package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver

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

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}
