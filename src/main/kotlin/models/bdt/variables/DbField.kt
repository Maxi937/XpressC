package models.bdt.variables

import com.gitlab.mvysny.konsumexml.Konsumer

data class DbField(
    val columnName: String,
) {
    companion object {
        fun xml(k: Konsumer): DbField {
            k.checkCurrent("DBField")
            val columnName = k.attributes.getValue("columnName")
            return DbField(columnName)
        }
    }
}