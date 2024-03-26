package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class Label(
    val name: String,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): Label {
            k.checkCurrent("Label")
            val name = k.attributes.getValue("name")
            return Label(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        bdtSolver.addActionToSequence(this)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}