package models.syntax.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import interfaces.Action
import interfaces.Container
import interfaces.whichAction
import models.compiler.Compiler
import models.compiler.Instructions
import models.compiler.Node
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

    override fun toJson(): JSONObject {
//        val result = JSONObject()
//
//
//        val block = JSONArray()
//        instructions.forEach {
//            val obj = JSONObject()
//            obj.put(it.javaClass.simpleName, it.toJson())
//            block.put(obj)
//        }
//
//        result.put("block", block)

        return JSONObject(this)
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
