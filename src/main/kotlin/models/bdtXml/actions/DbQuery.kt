package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.conditions.Comparison
import models.bdtXml.DbTable
import models.bdtXml.variables.RecordSetVar

data class DbQuery(

    val dataSourceName: String,
    val dsGroupName: String,
    val recordSetVar: RecordSetVar?,
    val fromTables: List<DbTable>,
    val conditions: List<Comparison>
) : Action {
    companion object {
        fun xml(k: Konsumer): DbQuery {
            k.checkCurrent("DBQuery")

            val dataSourceName = k.attributes.getValue("dataSourceName")
            val dsGroupName = k.attributes.getValue("dsGroupName")

            var recordSetVar: RecordSetVar? = null
            val fromTables: ArrayList<DbTable> = ArrayList()
            val conditions: ArrayList<Comparison> = ArrayList()


            k.allChildrenAutoIgnore(Names.of("RecordsetVar", "FromTables", "WhereCondition")) {
                when (localName) {
                    "RecordsetVar" -> recordSetVar = RecordSetVar.xml(this)
                    "FromTables" -> this.children("DBTable") { fromTables.add(DbTable.xml(this)) }
                    "WhereCondition" -> this.children("Comparison") { conditions.add(Comparison.xml(this)) }
                }
            }

            return DbQuery(dataSourceName, dsGroupName, recordSetVar, fromTables, conditions)
        }
    }

    override fun evaluate() {
        println(this)
    }
}



