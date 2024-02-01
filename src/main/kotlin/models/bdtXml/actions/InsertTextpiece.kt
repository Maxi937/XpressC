package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.Content.ContentItem
import models.BdtSolver
import models.bdtXml.ObjectRefListVar

data class InsertTextpiece(
    val name: String, val noOfObject: String, val requiredFlag: String, val objectRefListVar: ObjectRefListVar, var contentItem: ContentItem? = null
) : Action {
    companion object {
        fun xml(k: Konsumer): InsertTextpiece {
            k.checkCurrent("InsertTextpiece")

            val name: String = k.attributes.getValue("name")
            val noOfObject: String = k.attributes.getValue("noOfObject")
            val requiredFlag: String = k.attributes.getValue("requiredFlag")
            var objectRefListVar: ObjectRefListVar? = null

            k.child("ObjectRefListVar") {
                objectRefListVar = ObjectRefListVar.xml(this)
            }

            return InsertTextpiece(name, noOfObject, requiredFlag, objectRefListVar!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        contentItem = bdtSolver.bindContentItem(name, requiredFlag.toBoolean())
        bdtSolver.addActionToSequence(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}