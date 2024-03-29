package models.bdtXml.block

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.actions.Action
import models.bdtXml.actions.whichAction
import models.bdtXml.compiler.Compiler
import models.bdtXml.compiler.Instructions
import models.bdtXml.compiler.Node
import models.bdtXml.containers.Container
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

data class Block(
    override var instructions: Instructions<Action>,
    override var uuid: UUID = UUID.randomUUID(),
    private var parent: Node<Action>? = null,
) : Container {

    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun setup(compiler: Compiler) {
        instructions.forEach { it ->
            it.setup(compiler)
        }
    }


    fun toJsonArray(): JSONArray {
        val result = JSONArray()

        instructions.forEach { it ->
            val obj = JSONObject()
            obj.put(it.javaClass.simpleName, it.toJson())
            result.put(obj)
        }

        return result
    }

    override fun toJson(): JSONObject {
        val obj = JSONArray()
        instructions.forEach { it ->
            obj.put(it.toJson())
        }

        val res = JSONObject()
        res.put("block", obj)

        return res
    }

    override fun copy(): Container {
        return this.copy(instructions)
    }

    fun containsAll(elements: Collection<Action>): Boolean {
        return instructions.containsAll(elements)
    }

    fun contains(element: Action): Boolean {
        return instructions.contains(element)
    }

    fun isEmpty(): Boolean {
        return instructions.isEmpty()
    }

    fun getIntructions(): Instructions<Action> {
        return instructions
    }


    companion object {
        fun xml(k: Konsumer): Block {
            k.checkCurrent("Block")

            val instructions = Instructions<Action>()

            k.children(Names.any()) {
                val action = whichAction(this)

                if (action != null) {
                    instructions.append(action)
                }

            }

            return Block(instructions)
        }

    }
}
