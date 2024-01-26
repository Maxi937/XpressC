package models.bdtXml.variables

import com.gitlab.mvysny.konsumexml.Konsumer

data class Value(
    val dType: String,
    val value: String?
) {
    companion object {
        fun xml(k: Konsumer): Value {
            k.checkCurrent("Value")
            val dType = k.attributes.getValue("dtype")
            val value = k.text()
            return Value(dType, value)
        }
    }
}