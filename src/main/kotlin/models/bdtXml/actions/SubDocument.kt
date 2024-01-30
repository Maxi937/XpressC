package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.BdtSolver
import models.bdtXml.Key

data class SubDocument(
    val mappingType: String,
    val name: String,
    val key: Key
) : Action {
    companion object {
        fun xml(k: Konsumer): SubDocument {
            k.checkCurrent("SubDocument")

            val mappingType = k.attributes.getValue("mappingType")
            val name = k.attributes.getValue("sdName")
            var key: Key? = null

            k.child("Key") {
                key = Key.xml(this)

            }

            return SubDocument(mappingType, name, key!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        bdtSolver.launchSubdocument(name, key)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }

}