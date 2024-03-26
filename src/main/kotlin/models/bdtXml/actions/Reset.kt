package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.ObjectRefListVar
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class Reset(
    val objectRefListVar: ObjectRefListVar,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): Reset {
            k.checkCurrent("Reset")

            var objectRefListVar: ObjectRefListVar? = null

            k.child("ObjectRefListVar") {
                objectRefListVar = ObjectRefListVar.xml(this)
            }

            return Reset(objectRefListVar!!)
        }
    }

    // Might need to evaluate the objectList var by name but for now will just generally always reset crLength

    override fun evaluate(bdtSolver: BdtSolver) {
        if (objectRefListVar.name == "DLSTP") {
            evaluated = true
            bdtSolver.crLength = 0
        }
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }
}