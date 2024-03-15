package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.BdtSolver
import org.json.JSONArray
import org.json.JSONObject

data class And(
    val conditions: ArrayList<Condition>
) : Condition {
    companion object {
        fun xml(k: Konsumer): And {
            k.checkCurrent("And")

            val conditions: ArrayList<Condition> = ArrayList()

            k.children(Names.any(), 2, 2) {
                val condition = whichCondition(this)

                if(condition != null) {
                    conditions.add(condition)
                }
            }
            return And(conditions)
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
    override fun evaluate(bdtSolver: BdtSolver): Boolean {
        return conditions[0].evaluate(bdtSolver) && conditions[1].evaluate(bdtSolver)
    }
}