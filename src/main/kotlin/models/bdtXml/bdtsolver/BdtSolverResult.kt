package models.bdtXml.bdtsolver

import models.bdtXml.actions.Action
import models.bdtXml.actions.InsertTextpiece
import models.bdtXml.containers.If
import models.bdtXml.containers.Section
import models.bdtXml.containers.SubDocument
import models.bdtXml.variables.Var
import org.json.JSONArray
import org.json.JSONObject

class BdtSolverResult(
    /**
     * This is the sequence that was passed to the solver. This sequence will include every action that could have happened within the BDT.
     * **/
    val solvedSequence: ArrayList<Action>,
    /**
     * This is a FLAT Array of each evaluated action that happened when solving the BDT. Only TOP level items are evaluated elements, the sub
     * properties can be ignored.
     * **/
    val eventSequence: ArrayList<Action>,

    private val runtimeVariables: ArrayList<Var>,

    private val candidate: String
) {
    fun toJson(): JSONObject {
        val response = JSONObject()
        response.put("all", this.getAll())
        response.put("solved", this.getTaken())
        response.put("runtimeVariables", runtimeVariables)
        response.put("candidate", candidate)
        return response
    }

    private fun getAll(): JSONObject {
        val solved = JSONObject()

        val s = JSONArray()
        solvedSequence.forEach {
            val item = this.wrapActionAsJsonObject(it)
            s.put(item)
        }

        solved.put("sequence", s)

        val contentItems = getContentItemsFromAction(solvedSequence)
        solved.put("contentItems", JSONArray(contentItems))

        return solved
    }

    private fun getTaken(): JSONObject {
        val taken = JSONObject()

        val revisionUnits = ArrayList<String>()

        val s = JSONArray()
        eventSequence.forEach {
            val item = wrapActionAsJsonObject(it)
            s.put(item)

            if (it is Section) {
                revisionUnits += it.revisionUnits
            }
        }

        taken.put("eventSequence", s)
        taken.put("revisionUnits", revisionUnits)

        val contentItems = JSONArray()
        eventSequence.filterIsInstance<InsertTextpiece>().forEach {
            val item = wrapActionAsJsonObject(it)
            contentItems.put(item)
        }

        taken.put("contentItems", contentItems)
        return taken
    }

    private fun wrapActionAsJsonObject(obj: Action): JSONObject {
        val item = JSONObject()
        item.put(obj.javaClass.simpleName, obj.toJson())
        return item
    }

    private fun wrapVarAsJsonObject(obj: Var): JSONObject {
        val item = JSONObject()
        item.put(obj.javaClass.simpleName, obj)
        return item
    }

    private fun getContentItemsFromAction(actions: ArrayList<Action>): ArrayList<InsertTextpiece> {
        val contentItems: ArrayList<InsertTextpiece> = ArrayList()

        actions.forEach {
            when (it) {
                is InsertTextpiece -> contentItems.add(it)
                is Section -> {
                    if (it.block != null) {
                        contentItems += getContentItemsFromAction(it.block.actions)
                    }
                }

                is SubDocument -> {
                    contentItems += getContentItemsFromAction(it.block)
                }

                is If -> {
                    it.blocks.forEach { ifblock ->
                        contentItems += getContentItemsFromAction(ifblock.actions)
                    }
                }
            }
        }
        return contentItems
    }
}

