package models.compiler


import interfaces.Action
import interfaces.UnitTestInterface
import models.syntax.actions.InsertTextpiece
import models.syntax.containers.Section
import interfaces.Var
import models.unittest.RevisionUnitTest
import org.json.JSONArray
import org.json.JSONObject

class CompilerResult(
    val sequence: ArrayList<Action>,
    val fullEventSequence: ArrayList<Action>,
    val contentItems: ArrayList<InsertTextpiece>,
    val runtimeVariables: ArrayList<Var>,
    val tests: ArrayList<UnitTestInterface> = ArrayList()
) {


    fun toJson(): JSONObject {
        val response = JSONObject()
        response.put("sequence", this.getBdtSequence())
//        response.put("solved", this.getTaken())
        response.put("runtimeVariables", runtimeVariables)
        response.put("content", getContentItems())
        response.put("revisionUnits", getRevisionUnits())
        response.put("tests", getTests())
        return response
    }

    private fun getTests(): JSONArray {
        val res = JSONArray()

        tests.forEach {
            res.put(it.toJson())
        }

        return res
    }

    fun getRevisionUnits(): List<String> {
        val units = ArrayList<String>()

        this.fullEventSequence.forEach {
            if(it is Section) {
                val unit = it.getRevisionUnit()
                if(unit != null) {
                    units.add(unit)
                }
            }

        }

        return units
    }

    private fun getContentItems() : JSONObject {
        val content = JSONObject()

        val contentItems = JSONArray()
        this.contentItems.forEach {
            val obj = wrapActionAsJsonObject(it)
            contentItems.put(obj)
        }
        content.put("displayedContentItems", contentItems)
        return content
    }

    private fun getBdtSequence() : JSONArray {
        val s = JSONArray()

        sequence.forEach { it ->
            val obj = wrapActionAsJsonObject(it)
            s.put(obj)
        }
        return s
    }



//    private fun executedSequence(): JSONObject {
//        val taken = JSONObject()
//
//        val revisionUnits = ArrayList<String>()
//
//        val s = JSONArray()
//
//        fullEventSequence.forEach {
//            val item = wrapActionAsJsonObject(it)
//            s.put(item)
//
//            if (it is Section) {
//                revisionUnits += it.revisionUnits
//            }
//        }
//
//        taken.put("eventSequence", s)
//        taken.put("revisionUnits", revisionUnits)
//
//        val contentItems = JSONArray()
//
//        fullEventSequence.filterIsInstance<InsertTextpiece>().forEach {
//            val item = wrapActionAsJsonObject(it)
//            contentItems.put(item)
//        }
//
//        taken.put("contentItems", contentItems)
//        return taken
//    }

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
}

