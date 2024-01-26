package models.bdt.variables

import com.gitlab.mvysny.konsumexml.Konsumer
import models.Bdt.Variables.Var

data class RecordSetVar(
    val name: String,
) : Var {
    companion object {
        fun xml(k: Konsumer): RecordSetVar {
            k.checkCurrent("RecordsetVar")
            val name = k.attributes.getValue("name")
            return RecordSetVar(name)
        }
    }

}