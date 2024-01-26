package models.bdtXml.variables

import com.gitlab.mvysny.konsumexml.Konsumer

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