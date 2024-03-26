package models.bdtXml.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.actions.Action
import models.bdtXml.bdtsolver.BdtSolver
import models.bdtXml.conditions.Condition
import models.bdtXml.conditions.whichCondition
import org.json.JSONObject

// Assumption that an IF element can contain at Most 2 block elements -> A block to evaluate if true (always first) and a
// block to evaluate if false. This is not indicated within the BDT itself but can be inferred by the content within the blocks.

data class If(
    val condition: Condition,
    val blocks: ArrayList<Block>,
    var evaluated: Boolean = false,
    override var sequenceId: Int = 0,
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

        if (condition.evaluate(bdtSolver)) {
            evaluated = true
            blocks[0].evaluate(bdtSolver)
        } else {
            evaluated = false
            if (blocks.size == 2) {
                blocks[1].evaluate(bdtSolver)
            }
        }
    }

    override fun setup(bdtSolver: BdtSolver) {
        super.setup(bdtSolver)
        blocks.forEach {
            it.setup(bdtSolver)
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put(condition.javaClass.simpleName, condition.toJson())
        obj.put("true", blocks[0].toJsonArray())

        if (blocks.size == 2) {
            obj.put("false", blocks[1].toJsonArray())
        }

        obj.put("sequenceId", sequenceId)
        obj.put("evaluated", evaluated)
        return obj
    }
}