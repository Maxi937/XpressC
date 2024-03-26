package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.ObjectRefListVar
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class InsertTextpiece(
    val name: String,
    val noOfObject: String,
    val requiredFlag: String,
    val objectRefListVar: ObjectRefListVar,
    var textClassId: Long = 0,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
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
        if (bdtSolver.crLength >= 1) {
            evaluated = true
            textClassId = bdtSolver.bindContentItem(name, requiredFlag.toBoolean())
            bdtSolver.addActionToSequence(this)
            bdtSolver.crLength -= 1
        }

    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}