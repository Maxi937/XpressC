package models.Bdt.Variables

import com.gitlab.mvysny.konsumexml.Konsumer

data class Variable(
    val dType: String?,
    val name: String,
    val value: String?
    ) : Var {
    companion object {
        fun xml(k: Konsumer): Variable {
            k.checkCurrent("Variable")
            val dType = k.attributes.getValueOrNull("dtype")
            val name = k.attributes.getValue("name")
            val value = k.text()
            return Variable(dType, name, value)
        }
    }
}