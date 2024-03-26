package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

// Loop

data class Jump(
    val toLabel: String,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): Jump {
            k.checkCurrent("Jump")
            val toLabel = k.attributes.getValue("toLabel")
            return Jump(toLabel)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        bdtSolver.addActionToSequence(this)
        bdtSolver.jump(toLabel)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}