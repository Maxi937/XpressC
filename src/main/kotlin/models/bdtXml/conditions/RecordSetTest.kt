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


    // The bdtSolver will return True for End of Document and False for Not End of Document
    // If this method returns True the condition calling it will be evaluated so it must return inversely for the correct outcome
    override fun evaluate(bdtSolver: BdtSolver): Boolean {

        when (operator) {
            "noteod" -> return bdtSolver.isNotEod(recordSetVar.name)
            "eod" -> return bdtSolver.isEod(recordSetVar.name)
        }
        return false
    }
}