package models

import exceptions.BdtException
import models.CandidateXml.DataSource
import models.CandidateXml.Query
import models.CandidateXml.RecordSet
import models.CandidateXml.Table
import models.Content.ContentItem
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import models.bdtXml.Key
import models.bdtXml.actions.*
import models.bdtXml.conditions.And
import models.bdtXml.conditions.Condition
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable
import utils.SubdocumentBdtProvider

class BdtSolver(
    private val sequenceToSolve: ArrayList<Action>,
    val dataSource: DataSource,
    private val contentDb: ContentItemsDb,
    private val bdtProvider: SubdocumentBdtProvider,
    private val variables: ArrayList<Var> = ArrayList(),
    private val basesequence: ArrayList<Action> = ArrayList()
) {
    private val sequence: ArrayList<Action> = ArrayList()
    var currentRule: String = "Begin"
    var activeRecordSet: RecordSet = dataSource.recordSets[0]

    fun go() {
        sequenceToSolve.forEach {
            it.gather(basesequence)
            it.evaluate(this)
        }
    }

    fun addActionToSequence(action: Action) {
//        println(action)
        if (action is Rule) {
            currentRule = action.name
        }
        sequence.add(action)
    }


    fun collectState(): BdtState {
        return BdtState(variables, dataSource, contentDb, basesequence, bdtProvider)
    }

    fun launchSubdocument(name: String, key: Key) {
        val bdtstr = bdtProvider.getBdt(name)
        val subdocBdt = Bdt.fromXmlString(bdtstr)

        val subSolver = subSolver(subdocBdt.sequence, this)
        subSolver.go()

        val (basesequence, sequence) = subSolver.result()

        this.basesequence += basesequence
        this.sequence += sequence
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

    fun bindContentItem(contentItemName: String, required: Boolean = false): ContentItem? {
        val situsState = getVariable("SITUS_STATE")?.value ?: throw BdtException("No Situs State Variable Bound")
        val contentItem = contentDb.getContentItem(contentItemName, situsState)

        if (contentItem == null && required) {
            throw Exception("Required Content Item $contentItemName, but was unable to bind from ContentDb")
        }

        return contentItem
    }

    fun jump(labelName: String) {
        val jump = sequence.find { it is Jump && it.toLabel == labelName }
        val label = sequence.find { it is Label && it.name == labelName }

        if (label != null) {
            val entryPoint = sequence.indexOf(label)
            val exitPoint = sequence.indexOf(jump)

            val loopSequence = ArrayList(sequence.slice(IntRange(entryPoint, exitPoint - 1)))
            val loopSolver = subSolver(loopSequence, this)

            loopSolver.activeRecordSet = activeRecordSet
            loopSolver.go()

            val (loopbasesequence, resolvedLoopSequence) = loopSolver.result()

            sequence.removeAll(loopSequence)
            sequence += resolvedLoopSequence
            basesequence += loopbasesequence
        }
    }

    fun getDbVariable(name: String): String? {
        return activeRecordSet.getDbField(name)
    }

    fun result(): Pair<ArrayList<Action>, ArrayList<Action>> {
        return (Pair(basesequence, sequence))
    }

    companion object {
        fun subSolver(sequenceToSolve: ArrayList<Action>, bdtSolver: BdtSolver): BdtSolver {
            val state = bdtSolver.collectState()
            return BdtSolver(
                sequenceToSolve,
                dataSource = state.dataSource,
                contentDb = state.contentItemsDb,
                variables = state.variables,
                bdtProvider = state.bdtProvider
            )
        }
    }
}
