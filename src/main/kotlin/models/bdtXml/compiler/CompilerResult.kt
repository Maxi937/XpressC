package models.bdtXml.compiler

import models.bdtXml.Sequence
import models.bdtXml.actions.Action
import models.bdtXml.actions.InsertTextpiece
import models.bdtXml.containers.Section
import models.bdtXml.variables.Var
import org.json.JSONArray
import org.json.JSONObject

class CompilerResult(
    val sequence: Sequence,
    val eventSequence: ArrayList<Action>,
    val contentItems: ArrayList<InsertTextpiece>,
    val runtimeVariables: ArrayList<Var>,
    val candidate: String
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
        sequence.execute { it ->
            val item = this.wrapActionAsJsonObject(it)
            s.put(item)
        }

        solved.put("sequence", s)

        val contentItems = getContentItemsFromAction(sequence)
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

    private fun getContentItemsFromAction(sequence: Sequence): ArrayList<InsertTextpiece> {
        val contentItems: ArrayList<InsertTextpiece> = ArrayList()

//        sequence.execute { it ->
//            when (it) {
//                is InsertTextpiece -> contentItems.add(it)
//
//                is Section -> {
//                    if (it.block != null) {
//                        contentItems += getContentItemsFromAction(it.block.actions)
//                    }
//                }
//
//                is SubDocument -> {
//                    contentItems += getContentItemsFromAction(it.block.actions)
//                }
//
//                is If -> {
//                    it.blocks.forEach { ifblock ->
//                        contentItems += getContentItemsFromAction(ifblock.actions)
//                    }
//                }
//            }
//        }
        return contentItems
    }
}

