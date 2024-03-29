package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.DbTable
import models.bdtXml.compiler.Compiler
import models.bdtXml.conditions.*
import models.bdtXml.variables.DbField
import models.bdtXml.variables.Variable
import models.datasource.Query
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

data class DbQuery(
    val dataSourceName: String,
    val dsGroupName: String,
    val recordSetVar: RecordSetVar,
    val fromTables: List<DbTable>,
    val condition: Condition,
    override var uuid: UUID = UUID.randomUUID()
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

    private fun comparisonToQuery(comparison: Comparison, bdtSolver: Compiler): Query {
        val variable = comparison.compares.find { it is Variable }
        variable!!.bind(bdtSolver)
        return Query(comparison.compares.find { it is DbField }!!.name, variable.value, comparison.operator)
    }

    private fun handleConditions(condition: Condition, compiler: Compiler): ArrayList<Query> {
        val queries = ArrayList<Query>()

        when (condition) {
            is Comparison -> queries.add(comparisonToQuery(condition, compiler))
            is And -> condition.conditions.forEach { queries += handleConditions(it, compiler) }
            is Or -> condition.conditions.forEach { queries += handleConditions(it, compiler) }
        }
        return queries
    }

    override fun evaluate(compiler: Compiler): Boolean {
        val record = recordSetVar.name.substring(recordSetVar.name.indexOf(":") + 1)
        val queries = handleConditions(condition, compiler)
        compiler.query(record, queries)
        return true
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put(condition.javaClass.simpleName, condition.toJson())
        obj.put("dataSourceName", dataSourceName)
        obj.put("dsGroupName", dsGroupName)
        obj.put("recordSetVar", recordSetVar)
        obj.put("fromTables", JSONArray(fromTables))
        obj.put("uuid", this.uuid)
        return obj
    }

    override fun copy(): Action {
        return this.copy(dataSourceName, dsGroupName, recordSetVar, fromTables, condition, uuid = uuid)
    }

}



