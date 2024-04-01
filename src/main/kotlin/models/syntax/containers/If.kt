package models.syntax.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import models.compiler.Compiler
import models.compiler.Instructions
import interfaces.Condition
import interfaces.Container
import interfaces.whichCondition
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

// Assumption that an IF element can contain at Most 2 block elements -> A block to evaluate if true (always first) and a
// block to evaluate if false. This is not indicated within the BDT itself but can be inferred by the content within the blocks.

data class If(
    val condition: Condition,
    val trueInstructions: Instructions<Action> = Instructions(),
    val falseInstructions: Instructions<Action> = Instructions(),
    override var uuid: UUID = UUID.randomUUID(),
) : Container {
    override val instructions: Instructions<Action>
        get() = trueInstructions

    override fun evaluate(compiler: Compiler): Boolean {
        return condition.evaluate(compiler)
    }

    override fun setup(compiler: Compiler) {
        trueInstructions.forEach {
            it.setup(compiler)
        }

        falseInstructions.forEach {
            it.setup(compiler)
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()

        val condition = JSONObject()
        condition.put(this.condition.javaClass.simpleName, this.condition.toJson())
        obj.put("condition", condition)

        val trueBlock = JSONArray()
        trueInstructions.forEach {
            val trueObj = JSONObject()
            trueObj.put(it.javaClass.simpleName, it.toJson())
            trueBlock.put(trueObj)
        }
        obj.put("true", trueBlock)


        if(falseInstructions.isNotEmpty()) {
            val falseBlock = JSONArray()
            falseInstructions.forEach {
                val falseObj = JSONObject()
                falseObj.put(it.javaClass.simpleName, it.toJson())
                falseBlock.put(falseObj)
            }
            obj.put("false", falseBlock)
        }

        obj.put("false", falseInstructions)





        return obj
    }

    override fun copy(): Action {
        return this.copy(condition, trueInstructions, falseInstructions, UUID.randomUUID())
    }

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

            val trueInstructions = blocks[0].getIntructions()

            val falseInstructions: Instructions<Action> = if (blocks.size > 1) {
                blocks[1].getIntructions()
            } else {
                Instructions()
            }

            return If(condition, trueInstructions, falseInstructions)
        }
    }


}