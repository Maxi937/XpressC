package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import org.json.JSONObject

data class Rule(
    val name: String
) : Action {
    companion object {
        fun xml(k: Konsumer): Rule {
            k.checkCurrent("CurrentRule")
            val name: String = k.attributes.getValue("name")
            return Rule(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
    }

    override fun toJson(): JSONObject {
        val result = JSONObject(this)
        result.put("type", this.javaClass.simpleName)
        return result
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}
