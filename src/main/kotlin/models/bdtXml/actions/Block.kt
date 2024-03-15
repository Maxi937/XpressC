package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.BdtSolver
import org.json.JSONArray
import org.json.JSONObject

data class Block(
    val actions: ArrayList<Action>,
) : Action {
    companion object {
        fun xml(k: Konsumer): Block {
            k.checkCurrent("Block")

            val actions: ArrayList<Action> = ArrayList()

            k.children(Names.any()) {
                val action = whichAction(this)

                if (action != null) {
                    actions.add(action)
                }

            }

            return Block(actions)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        actions.forEach {
            it.evaluate(bdtSolver)
        }
    }

    fun toJsonArray() : JSONArray {
        val result = JSONArray()

        actions.forEach {
            val obj = JSONObject()
            obj.put(it.javaClass.simpleName, it.toJson())
            result.put(obj)
        }
        return result
    }

    override fun toJson() : JSONObject {
        val result = JSONObject()

        actions.forEach {
            result.put(this.javaClass.simpleName, it.toJson())
        }
        return result
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        actions.forEach {
            it.gather(sequence)
        }
        return sequence
    }
}
