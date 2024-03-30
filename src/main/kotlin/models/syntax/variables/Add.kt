package models.syntax.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Var
import models.compiler.Compiler
import java.util.*


data class Add(
    override val name: String,
    override var value: String,
    override var dType: String,
    val variables: List<Var>,
) : Var {
    companion object {
        fun xml(k: Konsumer): Add {
            k.checkCurrent("Add")

            val variables: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.any()) {
                when (localName) {
                    "Variable" -> variables.add(Variable.xml(this))
                    "Value" -> variables.add(Value.xml(this))
                    "Multiply" -> variables.add(Multiply.xml(this))
                    "Add" -> variables.add(Add.xml(this))
                }
            }
            return Add(UUID.randomUUID().toString(), variables[0].value, variables[0].dType, variables)
        }
    }

    override fun bind(bdtSolver: Compiler): Var {
        variables.forEach {
            it.bind(bdtSolver)
        }
        return calculate()
    }

    private fun calculate(): Var {
        when (variables[0].dType) {
            "float" -> variables[0].value = (variables[0].value.toFloat() + variables[1].value.toFloat()).toString()
            "int" -> variables[0].value = (variables[0].value.toInt() + variables[1].value.toInt()).toString()
        }

        return variables[0]
    }
}