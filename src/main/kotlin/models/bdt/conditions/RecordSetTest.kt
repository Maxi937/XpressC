package models.bdt.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdt.variables.RecordSetVar

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

    override fun evaluate() : Boolean {
        print(this)
        return true
    }
}