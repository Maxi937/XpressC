package models.bdtXml.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import java.util.*

data class Value(
    override var dType: String = "null",
    override var value: String = "",
    override val name: String
) : Var {
    companion object {
        fun xml(k: Konsumer): Value {
            k.checkCurrent("Value")
            val dType = k.attributes.getValue("dtype")
            val value = k.text()
            return Value(dType, value, UUID.randomUUID().toString())
        }
    }

    override fun bind(bdtSolver: BdtSolver): Var? {
        if (value.isEmpty()) {
            return null
        }
        return this
    }

//    override fun set(value: String, bdtState: BdtState): Var {
//        throw Exception("Cannot Set a Value")
//    }
}