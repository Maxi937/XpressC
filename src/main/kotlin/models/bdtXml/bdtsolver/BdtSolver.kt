package models.bdtXml.bdtsolver

import models.bdtXml.Bdt
import models.bdtXml.Key
import models.bdtXml.actions.Action
import models.bdtXml.actions.Jump
import models.bdtXml.actions.Label
import models.bdtXml.actions.Rule
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable
import models.bdtassetprovider.BdtAssetProvider
import models.datasource.DataSource
import models.datasource.Query
import models.datasource.RecordSet
import kotlin.system.exitProcess

class BdtSolver(
    private val name: String,
    private val sequenceToSolve: ArrayList<Action>,
    val dataSource: DataSource,
    private val assetProvider: BdtAssetProvider,
    private val variables: ArrayList<Var> = ArrayList(),
) {
    private var solvedSequence: ArrayList<Action> = ArrayList()
    private val eventSequence: ArrayList<Action> = ArrayList()
    private var currentRule: String = "Begin"
    var activeRecordSet: RecordSet = dataSource.recordSets[0]
    val crRequests: ArrayList<Long> = ArrayList()
    var crLength: Int = 0

    fun solve(): BdtSolverResult {
        sequenceToSolve.forEach {
            solvedSequence.add(it)
            it.setup(this)
            it.evaluate(this)
        }
        return BdtSolverResult(solvedSequence, eventSequence, variables, dataSource.name)
    }

    fun addActionToSequence(action: Action) {

        if (action is Rule) {
            currentRule = action.name
        }

        eventSequence.add(action)
        action.sequenceId = eventSequence.size
    }


    fun collectState(): BdtState {
        return BdtState(name, variables, dataSource, activeRecordSet, assetProvider)
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

    fun bindContentItem(contentItemName: String, required: Boolean = false): Long {
        return crRequests.last()
    }

    fun jump(labelName: String) {
        val jump = eventSequence.find { it is Jump && it.toLabel == labelName }
        val label = eventSequence.find { it is Label && it.name == labelName }

        if (label != null) {
            val entryPoint = eventSequence.indexOf(label)
            val exitPoint = eventSequence.indexOf(jump)

            val loopSequence = ArrayList(eventSequence.slice(IntRange(entryPoint, exitPoint - 1)))

            loopSequence.forEach {
                println(it)
            }

            exitProcess(0)
            val loopSolver = subSolver(loopSequence, this)
            val result = loopSolver.solve()

            result.eventSequence.forEach { println(it) }
        }
    }

    fun getDbVariable(name: String): String? {
        return activeRecordSet.getDbField(name)
    }


    companion object {
        fun subSolver(sequenceToSolve: ArrayList<Action>, bdtSolver: BdtSolver): BdtSolver {
            val state = bdtSolver.collectState()

            val solver = BdtSolver(
                name = state.name,
                sequenceToSolve,
                dataSource = state.dataSource,
                assetProvider = state.assetProvider,
                variables = state.variables,
            )

            solver.activeRecordSet = state.activeRecordSet
            return solver
        }
    }
}
