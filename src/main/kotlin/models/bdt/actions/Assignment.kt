package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdt.variables.Value
import models.bdt.variables.Variable

data class Assignment(
    val assignments: ArrayList<Any>,
) : Action {
    companion object {
        fun xml(k: Konsumer): Assignment {
            k.checkCurrent("Assign")

            val assignments: ArrayList<Any> = ArrayList()

            k.allChildrenAutoIgnore(Names.of("Variable", "Value")) {
                when (localName) {
                    "Variable" -> assignments.add(Variable.xml(this))
                    "Value" -> assignments.add(Value.xml(this))
                }
            }
            return Assignment(assignments)
        }
    }

    override fun evaluate() {
        println(this)
    }
}