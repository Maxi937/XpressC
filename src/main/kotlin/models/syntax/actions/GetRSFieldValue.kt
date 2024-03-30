package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import models.compiler.Compiler
import models.syntax.variables.DbField
import models.syntax.variables.Variable
import org.json.JSONObject
import java.util.*


data class GetRSFieldValue(
    val recordSetVar: RecordSetVar,
    val dbField: DbField,
    val variable: Variable,
    override var uuid: UUID = UUID.randomUUID()
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

    override fun evaluate(compiler: Compiler): Boolean {
        recordSetVar.evaluate(compiler)
        dbField.bind(compiler)
        variable.value = dbField.value
        compiler.bindVariable(variable)
        variable.bind(compiler)
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(recordSetVar, dbField, variable, uuid = uuid)
    }
}
