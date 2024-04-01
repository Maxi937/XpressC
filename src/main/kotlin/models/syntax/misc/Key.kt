package models.syntax.misc

import com.gitlab.mvysny.konsumexml.Konsumer

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