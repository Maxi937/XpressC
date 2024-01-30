package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.BdtSolver
import models.bdtXml.variables.Value
import models.bdtXml.variables.Var
import models.bdtXml.variables.Variable

data class Assignment(
    val assignments: ArrayList<Var>,
) : Action {
    companion object {
        fun xml(k: Konsumer): Assignment {
            k.checkCurrent("Assign")

            val assignments: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.of("Variable", "Value")) {
                when (localName) {
                    "Variable" -> assignments.add(Variable.xml(this))
                    "Value" -> assignments.add(Value.xml(this))
                }
            }
            return Assignment(assignments)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.assignVariable(assignments[0], assignments[1])
        bdtSolver.addActionToSequence(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}