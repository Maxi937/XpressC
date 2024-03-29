package models.bdtXml.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.Key
import models.bdtXml.actions.Action
import models.bdtXml.compiler.Compiler
import models.bdtXml.compiler.Instructions
import org.json.JSONObject
import java.util.*

data class SubDocument(
    var documentName: String?,
    val mappingType: String,
    val documentId: Long,
    val key: Key,
    override val instructions: Instructions<Action>,
    override var uuid: UUID = UUID.randomUUID(),
) : Container {

    private var isSetUp: Boolean = false


    override fun evaluate(compiler: Compiler): Boolean {
//        block.evaluate(compiler)
        return true
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("documentName", documentName)
        obj.put("documentId", documentId)
        obj.put("key", key)
//        obj.put("block", this.block.toJsonArray())
        return obj
    }

    override fun setup(compiler: Compiler) {
        if (!isSetUp) {
            val subdocument = compiler.getSubdocument(documentId, key)
            documentName = subdocument.name

            subdocument.sequence.execute { it ->
                instructions.append(it)
            }
            isSetUp = true
        }
    }

    override fun copy(): Action {
        return this.copy(documentName, mappingType, documentId, key, instructions, uuid)
    }

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

            return SubDocument(documentName, mappingType, documentId.toLong(), key!!, Instructions())
        }
    }
}