package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import interfaces.Var
import models.compiler.Compiler
import models.syntax.variables.*
import org.json.JSONArray
import org.json.JSONObject
import java.util.*


data class Assignment(
    val assignments: ArrayList<Var>,
    override var uuid: UUID = UUID.randomUUID()
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

    override fun evaluate(compiler: Compiler): Boolean {
        assignments.forEach {
            it.bind(compiler)
        }
        compiler.assignVariable(assignments[0], assignments[1])
        return true
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

    override fun copy(): Action {
        return this.copy(this.assignments)
    }

}