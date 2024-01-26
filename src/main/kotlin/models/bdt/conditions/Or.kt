package models.bdt.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names

data class Or(
    val conditions: ArrayList<Condition>
) : Condition {
    companion object {
        fun xml(k: Konsumer): Or {
            k.checkCurrent("Or")

            val conditions: ArrayList<Condition> = ArrayList()

            k.children(Names.any(), 2, 2) {
                val condition = whichCondition(this)

                if(condition != null) {
                    conditions.add(condition)
                }
            }
            return Or(conditions)
        }
    }

    override fun evaluate(): Boolean {
        println(conditions[0])
        println(conditions[1])
        return true
    }
}