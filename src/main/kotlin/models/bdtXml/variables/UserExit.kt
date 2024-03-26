package models.bdtXml.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver

data class UserExit(
    override var dType: String = "null",
    override val name: String,
    var parameters: ArrayList<Var> = ArrayList(),
    override var value: String = ""
) : Var {
    companion object {
        fun xml(k: Konsumer): UserExit {
            k.checkCurrent("Variable")
            val params = ArrayList<Var>()
            val dType = k.attributes.getValueOrNull("dtype")
            val name = k.attributes.getValue("name")

            k.children("Parm") {
                params.add(Parameter.xml(this))
            }

            return UserExit(dType.toString(), name, params)
        }
    }

    override fun bind(bdtSolver: BdtSolver): UserExit {
        value = bdtSolver.getVariable(name)?.value.toString()
        return this
    }

}