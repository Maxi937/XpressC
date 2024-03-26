package models.bdtXml.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.actions.Action
import models.bdtXml.actions.whichAction
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONArray
import org.json.JSONObject

data class Block(
    val actions: ArrayList<Action>,
    override var sequenceId: Int = 0,
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

    override fun setup(bdtSolver: BdtSolver) {
        super.setup(bdtSolver)
        actions.forEach {
            it.setup(bdtSolver)
        }
    }

    fun toJsonArray(): JSONArray {
        val result = JSONArray()

        actions.forEach {
            val obj = JSONObject()
            obj.put(it.javaClass.simpleName, it.toJson())
            result.put(obj)
        }

        return result
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()

        actions.forEach {
            obj.put(this.javaClass.simpleName, it.toJson())
        }
        return obj
    }
}
