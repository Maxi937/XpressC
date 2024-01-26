package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
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

    override fun evaluate() : Boolean{
        print(this)
        return true
    }
}