package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.variables.RecordSetVar
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable

data class ReplaceVariables(
    val id: String?, val validThrough: String, val variables: List<Var>
) : Action {
    companion object {
        fun xml(k: Konsumer): ReplaceVariables {
            k.checkCurrent("ReplaceVariables")
            val id = k.attributes.getValueOrNull("id")
            val validThrough = k.attributes.getValue("validThrough")

            val variables: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.of("Variable", "RecordsetVar")) {
                when (localName) {
                    "RecordsetVar" -> variables.add(RecordSetVar.xml(this))
                    "Variable" -> variables.add(Variable.xml(this))
                }
            }
            return ReplaceVariables(id, validThrough, variables)
        }
    }

    override fun evaluate() {
        println(this)
    }
}
