package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.BdtSolver
import models.bdtXml.conditions.Condition
import models.bdtXml.conditions.whichCondition
import org.json.JSONObject

// Assumption that an IF element can contain at Most 2 block elements -> A block to evaluate if true (always first) and a
// block to evaluate if false. This is not indicated within the BDT itself but can be inferred by the content within the blocks.

data class If(
    val condition: Condition,
    val blocks: ArrayList<Block>,
) : Action {
    companion object {
        fun xml(k: Konsumer): If {
            k.checkCurrent("If")

            lateinit var condition: Condition
            val blocks = ArrayList<Block>()

            k.allChildrenAutoIgnore(Names.of("Condition", "Block")) {
                when (localName) {
                    "Condition" -> condition = whichCondition(this)!!
                    "Block" -> blocks.add(Block.xml(this))
                }
            }
            return If(condition, blocks)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        if(condition.evaluate(bdtSolver)) {
            blocks[0].evaluate(bdtSolver)
        } else {
            if(blocks.size == 2) {
                blocks[1].evaluate(bdtSolver)
            }
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put(condition.javaClass.simpleName, condition.toJson())
        obj.put("true", blocks[0].toJsonArray())

        if(blocks.size == 2) {
            obj.put("false", blocks[1].toJsonArray())
        }
        return obj
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        blocks.forEach {
            it.gather(sequence)
        }
        return sequence
    }
}