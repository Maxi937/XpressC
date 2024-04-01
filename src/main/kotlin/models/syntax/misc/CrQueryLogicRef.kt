package models.syntax.misc

import com.gitlab.mvysny.konsumexml.Konsumer


data class CrQueryLogicRef(
    val name: String?
) {
    companion object {
        fun xml(k: Konsumer): CrQueryLogicRef {
            k.checkCurrent("CRQueryLogicRef")
            val name: String? = k.attributes.getValueOrNull("name")
            return CrQueryLogicRef(name)
        }
    }
}

