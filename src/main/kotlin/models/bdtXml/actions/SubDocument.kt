package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import models.bdtXml.Key
import org.json.JSONObject

data class SubDocument(
    val mappingType: String,
    val documentId: Long,
    val key: Key
) : Action {
    companion object {
        fun xml(k: Konsumer): SubDocument {
            k.checkCurrent("SubDocument")

            val mappingType = k.attributes.getValue("mappingType")
            val documentId = k.attributes.getValue("sdDocumentId")
            var key: Key? = null

            k.child("Key") {
                key = Key.xml(this)

            }

            return SubDocument(mappingType, documentId.toLong(), key!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        bdtSolver.launchSubdocument(documentId, key)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }

}