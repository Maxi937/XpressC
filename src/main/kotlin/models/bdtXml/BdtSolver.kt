package models.bdtXml

import exceptions.BdtException
import models.CandidateXml.DataSource
import models.CandidateXml.RecordSet
import models.Content.ContentItem
import models.Content.ContentItemsDb
import models.bdtXml.actions.*
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable

class BdtSolver(
    private val sequenceToSolve: ArrayList<Action>,
    private val dataSource: DataSource,
    private val contentDb: ContentItemsDb,
    private val variables: ArrayList<Var> = ArrayList(),
    private val basesequence: ArrayList<Action> = ArrayList()
) {
    private val sequence: ArrayList<Action> = ArrayList()
    private var currentRule: String = "Begin"
    private var activeRecordSet: RecordSet = dataSource.getRecordSet()

    fun go() {
        sequenceToSolve.forEach {
            it.gather(basesequence)
            it.evaluate(this)
        }
    }

    fun addActionToSequence(action: Action) {
        if (action is Rule) {
            currentRule = action.name
        }

        sequence.add(action)
        println(action)
    }

    fun collectState(): BdtState {
        return BdtState(variables, dataSource, contentDb, basesequence)
    }

    private fun getBdtFromRepository(name: String): String {
        return bdtFolder.getBdt(name)
    }

    // TODO - Launches BDT but state is not carried over
    // The key also contains a pointer to a variable that needs to passed as an input param
    fun launchSubdocument(name: String, key: Key) {
        val bdtstr = getBdtFromRepository(name)
        val subdocBdt = Bdt.fromXmlString(bdtstr)

        val subSolver = BdtSolver.subSolver(subdocBdt.sequence, this)

        subSolver.go()

        val (basesequence, sequence) = subSolver.result()

//        this.basesequence += basesequence
//        this.sequence += sequence
    }

    fun isEod(name: String): Boolean {
        val record = name.substring(name.indexOf(":") + 1)
        val recordSet = dataSource.getRecordSet(record)
        return recordSet.isEod()
    }

    fun isNotEod(name: String): Boolean {
        val record = name.substring(name.indexOf(":") + 1)
        val recordSet = dataSource.getRecordSet(record)
        return recordSet.isNotEod()
    }

    fun setRecordSet(name: String) {
        val record = name.substring(name.indexOf(":") + 1)
        activeRecordSet = dataSource.getRecordSet(record)
        println("Setting Record: $record")
    }

    fun recordSetMoveNext() {
        activeRecordSet.recordSetMoveNext()
    }

    // need to assign dtype probably
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

    // Indexing issue with loop, this loop is dependant on the actions that have been added to this sequence
    // This sequences actions are not one - to - one with the actual solving sequence
    // Te effect of this is that when individual actions are changed in what order they add to the sequence
    // This loop, loops indefinately - probably some issue with the entry and exit points
    fun jump(labelName: String) {
        val jump = sequence.find { it is Jump && it.toLabel == labelName}
        val label = sequence.find { it is Label && it.name == labelName }

        if (label != null) {
            val entryPoint = sequence.indexOf(label)
            val exitPoint = sequence.indexOf(jump)

            val loopSequence = ArrayList(sequence.slice(IntRange(entryPoint, exitPoint)))
            val loopSolver = BdtSolver.subSolver(loopSequence, this)
            loopSolver.go()

            loopSolver.sequence.forEach {
                println(it)
            }

        }
    }

    fun getDbVariable(name: String): String? {
        return activeRecordSet.getDbField(name)
    }

    fun result(): Pair<ArrayList<Action>, ArrayList<Action>> {
        return (Pair(basesequence, sequence))
    }

    companion object {
        fun subSolver(sequenceToSolve: ArrayList<Action>, bdtSolver: BdtSolver) : BdtSolver {
            val state = bdtSolver.collectState()
            return BdtSolver(sequenceToSolve, state.dataSource, state.contentItemsDb, state.variables)
        }
    }
}
