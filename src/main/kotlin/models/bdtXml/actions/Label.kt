package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import org.json.JSONObject

data class Label(
    val name: String,
    ) : Action {
    companion object {
        fun xml(k: Konsumer): Label {
            k.checkCurrent("Label")
            val name = k.attributes.getValue("name")
            return Label(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }

}