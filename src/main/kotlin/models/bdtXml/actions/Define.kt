package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import models.bdtXml.variables.Variable
import org.json.JSONObject


data class Define(
    val inputParameters: List<Variable>, val variables: List<Variable> = ArrayList(),
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false,
) : Action {
    companion object {
        fun xml(k: Konsumer): Define {
            k.checkCurrent("Define")
            val inputParams = k.child("InputParameters") { children("Variable") { Variable.xml(this) } }
            val variables = k.children("Variable") { Variable.xml(this) }
            return Define(inputParams, variables)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        variables.forEach {
            bdtSolver.bindVariable(it)
        }

        inputParameters.forEach {
            bdtSolver.bindInputParam(it)
        }
        bdtSolver.addActionToSequence(this)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }
}
