package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import models.bdtXml.variables.Variable

data class VariableTest(
    val operator: String,
    val variable: Variable,
) : Condition {
    companion object {
        fun xml(k: Konsumer): VariableTest {
            k.checkCurrent("VariableTest")

            val operator = k.attributes.getValue("operator")

            val variable: Variable = k.child("Variable") {
                return@child Variable.xml(this)
            }

            return VariableTest(operator, variable)
        }
    }

    private fun notNull(value: String?) : Boolean {
        return !(value.isNullOrEmpty() || value === "null")
    }

    override fun evaluate(bdtSolver: BdtSolver) : Boolean{
        variable.bind(bdtSolver)
        val value = variable.value

        return when(operator) {
            "notNull" -> notNull(value)
            else -> false
        }
    }
}