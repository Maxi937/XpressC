package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.variables.DbField
import models.bdtXml.variables.Value
import models.bdtXml.variables.Variable

data class Comparison(
    val operator: String,
    val compares: ArrayList<Any>,
) : Condition {
    companion object {
        fun xml(k: Konsumer): Comparison {
            k.checkCurrent("Comparison")

            val operator = k.attributes.getValue("operator")
            val compares: ArrayList<Any> = ArrayList()

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

    override fun evaluate() : Boolean {
        print(this)
        return true
    }
}