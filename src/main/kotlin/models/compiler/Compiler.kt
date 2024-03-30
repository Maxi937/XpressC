package models.compiler

import exceptions.BdtDataSourceRecordException
import models.Bdt
import models.syntax.misc.Key
import models.syntax.Sequence
import interfaces.Action
import models.syntax.actions.InsertTextpiece
import models.syntax.actions.Jump
import models.syntax.actions.Label
import models.syntax.containers.Block
import interfaces.Container
import models.syntax.containers.If
import interfaces.Var
import models.syntax.variables.Variable
import interfaces.AssetProviderInterface
import models.datasource.DataSource
import models.datasource.Query
import models.datasource.RecordSet

class Compiler(
    private val sequence: Sequence,
    val dataSource: DataSource,
    private val assetProvider: AssetProviderInterface,
    private val variables: ArrayList<Var> = ArrayList(),
) {
    private val instructions = Instructions<Action>()
    private val eventSequence: ArrayList<Action> = ArrayList()
    private var activeRecordSet: RecordSet = dataSource.recordSets[0]
    private val contentItems = ArrayList<InsertTextpiece>()
    private val labels: ArrayList<Node<Action>> = ArrayList()
    val crRequests: ArrayList<Long> = ArrayList()
    var crLength: Int = 0

    init {
        sequence.execute { it ->
            instructions.append(it)
        }
    }

    fun compile(): CompilerResult {
        return this.compile(instructions)
    }

    private fun compile(block: Block): CompilerResult {
        traverseInstructions(instructions)
        return CompilerResult(sequence, eventSequence, contentItems, variables, "dataSource")
    }

    private fun compile(instructions: Instructions<Action>): CompilerResult {
        val block = Block(instructions)
        return compile(block)
    }


    private fun addActionToEventSequence(action: Action) {
        val actionCopy = action.copy()

        eventSequence.add(actionCopy)

        if (actionCopy is InsertTextpiece) {
            bindContentItem(actionCopy)
        }
    }

    fun getSubdocument(documentId: Long, key: Key): Bdt {
        return assetProvider.getBdt(documentId)
    }

    fun isEod(name: String): Boolean {
        val record = name.substring(name.indexOf(":") + 1)
        val recordSet = dataSource.getRecordSet(record) ?: return true
        return recordSet.isEod()
    }

    fun isNotEod(name: String): Boolean {
        val record = name.substring(name.indexOf(":") + 1)
        val recordSet = dataSource.getRecordSet(record) ?: return false
        return recordSet.isNotEod()
    }

    fun recordSetMoveNext() {
        if (activeRecordSet.activeRecordSet.name == "EOF") {
            throw BdtDataSourceRecordException(activeRecordSet.name, dataSource)
        }
        activeRecordSet.recordSetMoveNext()
    }

    fun assignVariable(assignee: Var, assignor: Var): Var {
        val v = getVariable(assignee.name)

        return if (v != null) {
            v.value = assignor.value
            v
        } else {
            assignee.value = assignor.value
            bindVariable(assignee)
            assignee
        }
    }

    fun bindInputParam(variable: Variable) {
        if (variable.value.isEmpty()) {
            val dbValue = getDbVariable(variable.name)

            if (dbValue?.isNotEmpty() == true) {
                variable.value = dbValue
            }
        }
        variables.add(variable)
    }

    fun bindVariable(variable: Var) {
        val v = variables.find { it.name == variable.name }

        if (v != null) {
            v.value = variable.value
        } else {
            variables.add(variable)
        }
    }

    fun query(recordSetName: String, queries: ArrayList<Query>) {
        activeRecordSet = dataSource.query(recordSetName, queries)
    }

    fun setActiveRecord(recordSetName: String) {
        activeRecordSet = dataSource.getRecordSet(recordSetName)!!
    }

    fun getVariable(name: String): Var? {
        return variables.find { it.name.lowercase() == name.lowercase() }
    }

    private fun bindContentItem(contentItem: InsertTextpiece) {
        if (contentItem.textClassId > 1) {
            contentItems.add(contentItem)
        }
    }

    private fun handleContainer(action: Container) {
        when (action) {
            is If -> {
                if (action.evaluate(this)) {
                    traverseInstructions(action.trueInstructions)
                } else {
                    traverseInstructions(action.falseInstructions)
                }
            }

            else -> traverseInstructions(action.instructions)
        }
    }

    private fun handleJump(action: Jump) {
        var loop = labels.findLast { (it.value as Label).name == action.toLabel }

        while (loop!!.next != null) {
            if (loop.value.evaluate(this)) addActionToEventSequence(loop.value)
            val instruction = Instructions<Action>()
            instruction.append(loop.value)

            when (loop.value) {
                is Container -> if (!traverseInstructions(instruction, action)) break
                action -> break
            }
            loop = loop.next!!
        }
    }

    private fun traverseInstructions(instructions: Instructions<Action>, breakAt: Action? = null): Boolean {
        instructions.forEachIndexed { index, action ->
            action.setup(this)

            if (action.evaluate(this)) addActionToEventSequence(action)

            when (action) {
                is Jump -> handleJump(action)
                is Label -> labels.add(instructions.nodeAt(index)!!)
                is Container -> handleContainer(action)
                breakAt -> return false
            }
        }
        return true
    }

    fun getDbVariable(name: String): String? {
        return activeRecordSet.getDbField(name)
    }
}
