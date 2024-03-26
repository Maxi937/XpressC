package models.bdtXml.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.bdtsolver.BdtSolver
import java.util.*


data class Multiply(
    override val name: String,
    override var value: String,
    override var dType: String,
    val variables: List<Var>,
) : Var {
    companion object {
        fun xml(k: Konsumer): Multiply {
            k.checkCurrent("Multiply")

            val variables: ArrayList<Var> = ArrayList()

            k.allChildrenAutoIgnore(Names.any()) {
                when (localName) {
                    "Variable" -> variables.add(Variable.xml(this))
                    "Value" -> variables.add(Value.xml(this))
                    "Multiply" -> variables.add(Multiply.xml(this))
                    "Add" -> variables.add(Add.xml(this))
                }
            }
            return Multiply(UUID.randomUUID().toString(), variables[0].value, variables[0].dType, variables)
        }
    }

    override fun bind(bdtSolver: BdtSolver): Var {
        variables.forEach {
            it.bind(bdtSolver)
        }
        return calculate()
    }

    private fun calculate(): Var {
//        when(variables[0].dType) {
//            "float" -> variables[0].value = (variables[0].value.toFloat() * variables[1].value.toFloat()).toString()
//            "int" -> variables[0].value = (variables[0].value.toInt() * variables[1].value.toInt()).toString()
//        }

        return variables[0]
    }
}