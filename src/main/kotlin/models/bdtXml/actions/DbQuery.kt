package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.BdtSolver
import models.CandidateXml.Query
import models.bdtXml.DbTable
import models.bdtXml.conditions.*
import models.bdtXml.variables.DbField
import models.bdtXml.variables.Variable

data class DbQuery(

    val dataSourceName: String,
    val dsGroupName: String,
    val recordSetVar: RecordSetVar,
    val fromTables: List<DbTable>,
    val condition: Condition
) : Action {
    companion object {
        fun xml(k: Konsumer): DbQuery {
            k.checkCurrent("DBQuery")

            val dataSourceName = k.attributes.getValue("dataSourceName")
            val dsGroupName = k.attributes.getValue("dsGroupName")

            var recordSetVar: RecordSetVar? = null
            val fromTables: ArrayList<DbTable> = ArrayList()
            var condition: Condition? = null


            k.allChildrenAutoIgnore(Names.of("RecordsetVar", "FromTables", "WhereCondition")) {
                when (localName) {
                    "RecordsetVar" -> recordSetVar = RecordSetVar.xml(this)
                    "FromTables" -> this.children("DBTable") { fromTables.add(DbTable.xml(this)) }
                    "WhereCondition" -> condition = whichCondition(this)
                }
            }

            return DbQuery(dataSourceName, dsGroupName, recordSetVar!!, fromTables, condition!!)
        }
    }

    fun comparisonToQuery(comparison: Comparison, bdtSolver: BdtSolver): Query {
        val variable = comparison.compares.find { it is Variable }
        variable!!.bind(bdtSolver)
        return Query(comparison.compares.find { it is DbField }!!.name, variable.value, comparison.operator)
    }

    private fun handleConditions(condition: Condition, bdtSolver: BdtSolver): ArrayList<Query> {
        val queries = ArrayList<Query>()

        when(condition) {
            is Comparison -> queries.add(comparisonToQuery(condition, bdtSolver))
            is And -> condition.conditions.forEach { queries += handleConditions(it, bdtSolver)}
            is Or -> condition.conditions.forEach { queries += handleConditions(it, bdtSolver)}
        }
        return queries
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        val record = recordSetVar.name.substring(recordSetVar.name.indexOf(":") + 1)

        val queries = handleConditions(condition, bdtSolver)

        bdtSolver.query(record, queries)

    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}



