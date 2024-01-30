package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.BdtSolver
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

    // operate can be noteod - NOT END OF DOCUMENT
    // operator can probably be eod - IS END OF DOCUMENT
    override fun evaluate(bdtSolver: BdtSolver) : Boolean {
        bdtSolver.setRecordSet(recordSetVar.name)
        return false
    }
}