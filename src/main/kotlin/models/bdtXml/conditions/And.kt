package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.BdtSolver

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
    override fun evaluate(bdtSolver: BdtSolver): Boolean {
        return conditions[0].evaluate(bdtSolver) && conditions[1].evaluate(bdtSolver)
    }
}