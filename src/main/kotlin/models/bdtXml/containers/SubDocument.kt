package models.bdtXml.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.Key
import models.bdtXml.actions.Action
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONArray
import org.json.JSONObject

data class SubDocument(
    var documentName: String?,
    val mappingType: String,
    val documentId: Long,
    val key: Key,
    val block: ArrayList<Action> = ArrayList(),
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): SubDocument {
            k.checkCurrent("SubDocument")

            val mappingType = k.attributes.getValue("mappingType")
            val documentId = k.attributes.getValue("sdDocumentId")
            val documentName = k.attributes.getValueOrNull("sdDocumentName")
            var key: Key? = null

            k.child("Key") {
                key = Key.xml(this)
            }

            return SubDocument(documentName, mappingType, documentId.toLong(), key!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        evaluated = true
        block.forEach {
            it.evaluate(bdtSolver)
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("documentName", documentName)
        obj.put("documentId", documentId)
        obj.put("key", key)

        val actions = JSONArray()

        this.block.forEach {
            val item = JSONObject()
            item.put(it.javaClass.simpleName, it.toJson())
            actions.put(item)
        }

        obj.put("block", actions)
        obj.put("sequenceId", sequenceId)
        obj.put("evaluated", evaluated)

        return obj
    }

    override fun setup(bdtSolver: BdtSolver) {
        val subdocument = bdtSolver.getSubdocument(documentId, key)
        documentName = subdocument.name
        block += subdocument.sequence
        block.forEach {
            it.setup(bdtSolver)
        }
    }
}