package models.bdtXml.variables


import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler

data class DbField(
    override val name: String,
    override var value: String = "",
    override var dType: String = "field"
) : Var {
    companion object {
        fun xml(k: Konsumer): DbField {
            k.checkCurrent("DBField")
            val columnName = k.attributes.getValue("columnName")
            return DbField(columnName)
        }
    }

    override fun bind(bdtSolver: Compiler): DbField {
        value = bdtSolver.getDbVariable(name).toString()
        return this
    }

//    override fun set(value: String, bdtState: BdtState): Var {
//        throw Exception("Cannot set a DB Variable")
//    }
}