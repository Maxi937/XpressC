package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import models.bdtXml.actions.RecordSetVar

data class RecordSetTest(
    val operator: String,
    val recordSetVar: RecordSetVar,
) : Condition {
    companion object {
        fun xml(k: Konsumer): RecordSetTest {
            k.checkCurrent("RecordsetTest")

            val operator = k.attributes.getValue("operator")
            val recordSetVar: RecordSetVar = k.child("RecordsetVar") {
                return@child RecordSetVar.xml(this)
            }

            return RecordSetTest(operator, recordSetVar)
        }
    }


    override fun evaluate(bdtSolver: BdtSolver): Boolean {
        when (operator) {
            "noteod" -> return bdtSolver.isNotEod(recordSetVar.name)
            "eod" -> return bdtSolver.isEod(recordSetVar.name)
        }
        return false
    }
}