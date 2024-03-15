package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.BdtSolver
import models.bdtXml.variables.DbField
import models.bdtXml.variables.Value
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDate

data class Comparison(
    val operator: String,
    val compares: ArrayList<Var>,
) : Condition {
    companion object {
        fun xml(k: Konsumer): Comparison {
            k.checkCurrent("Comparison")

            val operator = k.attributes.getValue("operator")
            val compares: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.of("Variable", "DBField", "Value")) {
                when (localName) {
                    "Variable" -> compares.add(Variable.xml(this))
                    "DBField" -> compares.add(DbField.xml(this))
                    "Value" -> compares.add(Value.xml(this))
                }
            }
            return Comparison(operator, compares)
        }
    }

    private fun equals(variable1: Var, variable2: Var) : Boolean {
        return variable1.value == variable2.value
    }

    private fun notEquals(variable1: Var, variable2: Var) : Boolean {
        return variable1.value != variable2.value
    }

    private fun lessThan(variable1: Var, variable2: Var) : Boolean {
        when(variable1.dType) {
            "dateTime" -> return LocalDate.parse(variable1.value) < LocalDate.parse(variable2.value)
            "string" -> return variable1.value.toFloat() > variable2.value.toFloat()
        }
        return false
    }

    private fun greaterThan(variable1: Var, variable2: Var) : Boolean {
        when(variable1.dType) {
            "dateTime" -> return LocalDate.parse(variable1.value) > LocalDate.parse(variable2.value)
            "string" -> return variable1.value.toFloat() > variable2.value.toFloat()
        }
        return false
    }

    private fun lessThanOrEqualTo(variable1: Var, variable2: Var) : Boolean {
        when(variable1.dType) {
            "dateTime" -> return LocalDate.parse(variable1.value) <= LocalDate.parse(variable2.value)
            "string" -> return variable1.value.toFloat() <= variable2.value.toFloat()
        }
        return false
    }

    private fun greaterThanOrEqualTo(variable1: Var, variable2: Var) : Boolean {
        when(variable1.dType) {
            "dateTime" -> return LocalDate.parse(variable1.value) >= LocalDate.parse(variable2.value)
            "string" -> return variable1.value.toFloat() >= variable2.value.toFloat()
        }
        return false
    }


    private fun bind(bdtSolver: BdtSolver) {
        compares.forEach {
            it.bind(bdtSolver)
        }
    }

    private fun bothHaveValues() : Boolean {
        if(compares[0].value == "null" && compares[1].value != "null") {
            return false
        }

        if(compares[0].value != "null" && compares[1].value == "null") {
            return false
        }

        return true
    }

    override fun evaluate(bdtSolver: BdtSolver): Boolean {
        bind(bdtSolver)

        if(!bothHaveValues()) {
            return false
        }

//        println("Comparing: ${compares[0]} $operator ${compares[1]}")

        return when(operator) {
            "le" -> lessThanOrEqualTo(compares[0], compares[1])
            "ge" -> greaterThanOrEqualTo(compares[0], compares[1])
            "gt" -> greaterThan(compares[0], compares[1])
            "lt" -> lessThan(compares[0], compares[1])
            "ne" -> notEquals(compares[0], compares[1])
            "eq" -> equals(compares[0], compares[1])
            else -> false
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("operator", operator)

        val compares = JSONArray()

        this.compares.forEach {
            val cmp = JSONObject()
            cmp.put(it.javaClass.simpleName, JSONObject(it))
            compares.put(cmp)
        }

        obj.put("compares", compares)
        return obj
    }


}