package models.syntax.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import interfaces.Condition
import models.compiler.Compiler
import models.syntax.variables.Variable
import org.json.JSONObject

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

    private fun notNull(variable: Variable, value: String?): Boolean {
        return !(value.isNullOrEmpty() || value === "null")
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun evaluate(bdtSolver: Compiler): Boolean {
        variable.bind(bdtSolver)
        val value = variable.value

        return when (operator) {
            "notNull" -> notNull(variable, value)
            else -> false
        }
    }
}