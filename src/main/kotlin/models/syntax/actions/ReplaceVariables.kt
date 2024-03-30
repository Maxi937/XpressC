package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import models.compiler.Compiler
import models.syntax.variables.Variable
import org.json.JSONObject
import java.util.*


data class ReplaceVariables(
    val id: String?, val validThrough: String,
    val variables: List<Variable>,
    val recordSetVar: RecordSetVar? = null,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(id, validThrough, variables, recordSetVar, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): ReplaceVariables {
            k.checkCurrent("ReplaceVariables")
            val id = k.attributes.getValueOrNull("id")
            val validThrough = k.attributes.getValue("validThrough")

            val variables: ArrayList<Variable> = ArrayList()
            var recordSetVar: RecordSetVar? = null

            k.allChildrenAutoIgnore(Names.of("Variable", "RecordsetVar")) {
                when (localName) {
                    "RecordsetVar" -> recordSetVar = RecordSetVar.xml(this)
                    "Variable" -> variables.add(Variable.xml(this))
                }
            }
            return ReplaceVariables(id, validThrough, variables, recordSetVar)
        }
    }


}
