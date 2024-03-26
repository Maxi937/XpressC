package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.bdtsolver.BdtSolver
import models.bdtXml.variables.Variable
import org.json.JSONObject


data class ReplaceVariables(
    val id: String?, val validThrough: String,
    val variables: List<Variable>,
    val recordSetVar: RecordSetVar? = null,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
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

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
//        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }
}
