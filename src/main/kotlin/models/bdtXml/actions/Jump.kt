package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import org.json.JSONObject

// Loop

data class Jump(
    val toLabel: String,
    ) : Action {
    companion object {
        fun xml(k: Konsumer): Jump {
            k.checkCurrent("Jump")
            val toLabel = k.attributes.getValue("toLabel")
            return Jump(toLabel)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        bdtSolver.jump(toLabel)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }

}