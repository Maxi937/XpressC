package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.BdtSolver
import models.bdtXml.variables.*
import org.json.JSONArray
import org.json.JSONObject


data class Assignment(
    val assignments: ArrayList<Var>,
) : Action {
    companion object {
        fun xml(k: Konsumer): Assignment {
            k.checkCurrent("Assign")

            val assignments: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.any()) {
                when (localName) {
                    "Variable" -> assignments.add(Variable.xml(this))
                    "Value" -> assignments.add(Value.xml(this))
                    "Multiply" -> assignments.add(Multiply.xml(this))
                    "Add" -> assignments.add(Add.xml(this))
                }
            }
            return Assignment(assignments)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        assignments.forEach {
            it.bind(bdtSolver)
        }

        bdtSolver.assignVariable(assignments[0], assignments[1])
        bdtSolver.addActionToSequence(this)
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()

        val assignments = JSONArray()

        this.assignments.forEach {
            val ass = JSONObject()
            ass.put(it.javaClass.simpleName, JSONObject(it))
            assignments.put(ass)
        }

        obj.put("assignments", assignments)
        return obj
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}