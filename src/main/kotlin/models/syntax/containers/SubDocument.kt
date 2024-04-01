package models.syntax.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import models.syntax.misc.Key
import interfaces.Action
import interfaces.Container
import models.compiler.Compiler
import models.compiler.Instructions
import org.json.JSONArray
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
        return true
    }

    override fun toJson(): JSONObject {
        val result = JSONObject()
        result.put("documentName", documentName)
        result.put("documentId", documentId)
        result.put("key", key)

        val block = JSONArray()

        instructions.forEach {
            val obj = JSONObject()
            obj.put(it.javaClass.simpleName, it.toJson())
            block.put(obj)
        }

        result.put("block", block)
        return result
    }

    override fun setup(compiler: Compiler) {
        if (!isSetUp) {
            val subdocument = compiler.getSubdocument(documentId, key)
            documentName = subdocument.name

            subdocument.sequence.forEach { it ->
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