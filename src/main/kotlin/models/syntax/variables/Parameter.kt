package models.syntax.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import interfaces.Var
import models.compiler.Compiler

data class Parameter(
    override var dType: String = "null",
    override val name: String,
    override var value: String = ""
) : Var {
    companion object {
        fun xml(k: Konsumer): Parameter {
            k.checkCurrent("Parm")
            val dType = k.attributes.getValueOrNull("dtype")
            val name = k.attributes.getValue("name")
            val value = k.text()
            return Parameter(dType.toString(), name, value)
        }
    }

    override fun bind(bdtSolver: Compiler): Parameter {
        value = bdtSolver.getVariable(name)?.value.toString()
        return this
    }

}