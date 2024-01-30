package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.BdtSolver
import models.bdtXml.conditions.Condition
import models.bdtXml.conditions.whichCondition


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

    override fun evaluate(bdtSolver: BdtSolver) {
        if(condition?.evaluate(bdtSolver) == true) {
            block.evaluate(bdtSolver)
        }
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        block.gather(sequence)
        return sequence
    }
}