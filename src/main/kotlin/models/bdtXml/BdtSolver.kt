package models.bdtXml

import exceptions.BdtException
import models.CandidateXml.DataSource
import models.Content.ContentItem
import models.Content.ContentItemsDb
import models.bdtXml.actions.*
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable

class BdtSolver(private val bdt: BDT, private val dataSource: DataSource, private val contentDb: ContentItemsDb) {
    private val variables: ArrayList<Var> = ArrayList()
    private val labels: ArrayList<Label> = ArrayList()
    private val basesequence: ArrayList<Action> = ArrayList()
    private val sequence: ArrayList<Action> = ArrayList()
    private var recordSet: String = ""

    init {
        initialRecordSet()
    }

    private fun initialRecordSet() {
        bdt.sequence.forEach {
            if (it is DbQuery) {
                return setRecordSet(it.recordSetVar.name)
            }
        }
    }

    fun go() {
        bdt.sequence.forEach {
            it.gather(basesequence)
            it.evaluate(this)
        }
    }

    private fun getBdtFromRepository(name: String): String {
        return bdtFolder.getBdt(name)
    }

    // TODO - Launches BDT but state is not carried over
    // The key also contains a pointer to a variable that needs to passed as an input param
    fun launchSubdocument(name: String, key: Key) {
        val bdtstr = getBdtFromRepository(name)
        val subdocBdt = BDT.fromXmlString(bdtstr)

//        val state = BdtSolver(bdt, dataSource, contentDb)
//        state.bindInputParam()
        val (basesequence, sequence) = subdocBdt.solve(dataSource, contentDb)

        this.basesequence += basesequence
        this.sequence += sequence
    }

    fun setRecordSet(name: String) {
        val record = name.substring(name.indexOf(":") + 1)
        if (recordSet != record) {
            recordSet = record
        }
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
            if (!dbValue.isNullOrEmpty()) {
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

    fun addLabel(label: Label) {
       val l = labels.find { it.name == label.name }
        if(l == null) {
            labels.add(label)
        }
    }

    fun addActionToSequence(action: Action) {
        sequence.add(action)
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

    fun getDbVariable(name: String): String? {
        return dataSource.find(recordSet, name)
    }

    fun result(): Pair<ArrayList<Action>, ArrayList<Action>> {
        return (Pair(basesequence, sequence))
    }
}