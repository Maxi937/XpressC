package models.bdtXml

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

data class DbTable(
    val tableName: String
) {
    companion object {
        fun xml(k: Konsumer): DbTable {
            k.checkCurrent("DBTable")
            val tableName = k.attributes.getValue("tableName")
            return DbTable(tableName)
        }
    }
}