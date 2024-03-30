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

data class Key(
    val sdField: String,
    val sdGroup: String,
    val sdKeyName: String,
    val sdKeyType: String,
    val sTable: String
) {
    companion object {
        fun xml(k: Konsumer): Key {
            k.checkCurrent("Key")
            val sdField = k.attributes.getValue("sdField")
            val sdGroup = k.attributes.getValue("sdGroup")
            val sdKeyName = k.attributes.getValue("sdKeyName")
            val sdKeyType = k.attributes.getValue("sdKeyType")
            val sdTable = k.attributes.getValue("sdTable")
            return Key(sdField, sdGroup, sdKeyName, sdKeyType, sdTable)
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