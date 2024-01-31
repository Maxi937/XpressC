package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.BdtSolver
import models.bdtXml.conditions.Comparison
import models.bdtXml.DbTable
import models.bdtXml.conditions.Condition
import models.bdtXml.conditions.whichCondition

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

    override fun evaluate(bdtSolver: BdtSolver) {
        recordSetVar.evaluate(bdtSolver)

        if(condition.evaluate(bdtSolver)) {
            bdtSolver.addActionToSequence(this)
        } else {
//            throw Exception("${condition.compares}\nnot ${condition.operator}")
        }
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}



