package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class Rule(
    val name: String,
    var evaluated: Boolean = false,
    override var sequenceId: Int = 0,
) : Action {
    companion object {
        fun xml(k: Konsumer): Rule {
            k.checkCurrent("CurrentRule")
            val name: String = k.attributes.getValue("name")
            return Rule(name)
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
