package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdt.conditions.Condition
import models.bdt.conditions.whichCondition


data class If(
    val condition: Condition? = null,
    val block: Block,
) : Action {
    companion object {
        fun xml(k: Konsumer): If {
            k.checkCurrent("If")

            var condition: Condition? = null
            var block: Block? = null

            k.allChildrenAutoIgnore(Names.of("Condition", "Block")) {
                when (localName) {
                    "Condition" -> condition = whichCondition(this)
                    "Block" -> block = Block.xml(this)
                }
            }
            return If(condition, block!!)
        }
    }

    override fun evaluate() {
        println(this)
    }
}