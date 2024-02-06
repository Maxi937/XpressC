package models.CandidateXml

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

data class Table(val name: String, val columns: ArrayList<Column> = ArrayList()) {

    fun getField(columnName: String) : String? {
        val column = columns.find { it.name.lowercase() == columnName.lowercase() }
        return column?.value
    }

    fun getDbField(columnName: String) : String? {
        val column = columns.find { it.name.lowercase() == columnName.lowercase() }
        return column?.value
    }

    companion object {

        fun fromJson(tableName: String, data: JSONObject): ArrayList<Table> {
            val result = ArrayList<Table>()

            val table = Table(tableName)

            data.keys().forEach { key ->
                when (val obj = data.get(key)) {
                    is JSONObject -> result += fromJson(key, obj)
                    is JSONArray -> obj.forEach { result += fromJson(key, it as JSONObject) }
                    else -> table.columns.add(Column(key, obj.toString()))
                }

            }
            if(table.columns.isNotEmpty()) {
                result.add(table)
            }

            return result
        }
    }
}