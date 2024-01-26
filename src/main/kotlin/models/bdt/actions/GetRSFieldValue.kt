package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdt.variables.DbField
import models.bdt.variables.RecordSetVar
import models.bdt.variables.Variable


data class GetRSFieldValue(
    val recordSetVar: RecordSetVar,
    val dbField: DbField,
    val variable: Variable
) : Action {
    companion object {
        fun xml(k: Konsumer): GetRSFieldValue {
            k.checkCurrent("GetRSFieldValue")

            var variable: Variable? = null
            var recordSetVar: RecordSetVar? = null
            var dbField: DbField? = null

            k.allChildrenAutoIgnore(Names.of("RecordsetVar", "Variable", "DBField")) {
                when (localName) {
                    "RecordsetVar" -> recordSetVar = RecordSetVar.xml(this)
                    "Variable" -> variable = Variable.xml(this)
                    "DBField" -> dbField = DbField.xml(this)
                }
            }

            return GetRSFieldValue(recordSetVar!!, dbField!!, variable!!)
        }
    }

    override fun evaluate() {
        println(this)
    }
}
