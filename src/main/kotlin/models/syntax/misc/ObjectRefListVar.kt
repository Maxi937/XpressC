package models.syntax.misc

import com.gitlab.mvysny.konsumexml.Konsumer

data class ObjectRefListVar(val name: String) {
    companion object {
        fun xml(k: Konsumer): ObjectRefListVar {
            k.checkCurrent("ObjectRefListVar")
            val name: String = k.attributes.getValue("name")
            return ObjectRefListVar(name)
        }
    }
}