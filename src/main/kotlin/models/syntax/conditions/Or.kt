package models.syntax.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import interfaces.Condition
import interfaces.whichCondition
import models.compiler.Compiler
import org.json.JSONArray
import org.json.JSONObject

data class Or(
    val conditions: ArrayList<Condition>
) : Condition {
    companion object {
        fun xml(k: Konsumer): Or {
            k.checkCurrent("Or")

            val conditions: ArrayList<Condition> = ArrayList()

            k.children(Names.any(), 2, 2) {
                val condition = whichCondition(this)

                if (condition != null) {
                    conditions.add(condition)
                }
            }
            return Or(conditions)
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()

        val cnds = JSONArray()

        this.conditions.forEach {
            val cmp = JSONObject()
            cmp.put(it.javaClass.simpleName, it.toJson())
            cnds.put(cmp)
        }

        obj.put("conditions", cnds)
        return obj
    }

    override fun evaluate(bdtSolver: Compiler): Boolean {
//        println("OR: ${conditions[0].evaluate(bdtSolver) || conditions[1].evaluate(bdtSolver)}")
        return conditions[0].evaluate(bdtSolver) || conditions[1].evaluate(bdtSolver)
    }
}