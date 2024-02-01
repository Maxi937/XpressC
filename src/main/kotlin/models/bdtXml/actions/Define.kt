package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import models.bdtXml.variables.Variable


data class Define(val inputParameters: List<Variable>, val variables: List<Variable> = ArrayList()) : Action {
    companion object {
        fun xml(k: Konsumer): Define {
            k.checkCurrent("Define")
            val inputParams = k.child("InputParameters") { children("Variable") { Variable.xml(this) } }
            val variables = k.children("Variable") { Variable.xml(this) }
            return Define(inputParams, variables)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        variables.forEach {
            bdtSolver.bindVariable(it)
        }

        inputParameters.forEach {
            bdtSolver.bindInputParam(it)
        }

        bdtSolver.addActionToSequence(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}
