package models.syntax.misc

import com.gitlab.mvysny.konsumexml.Konsumer

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