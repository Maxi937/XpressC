package models.CandidateXml

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML

data class Table(val name: String, val columns: ArrayList<Column> = ArrayList(), val subTables: ArrayList<Table> = ArrayList()) {

    companion object {
        fun jsonDataToTables(json: JSONObject) : ArrayList<Table> {
            val tables = ArrayList<Table>()

            json.keys().forEach { property ->
                val table = Table(property.toString())
                val data = json.getJSONObject(table.name)

                data.keys().forEach { key ->
                    when (val obj = data.get(key)) {
                        is JSONObject -> tables.add(fromJson(key, obj))
                        is JSONArray -> obj.forEach { table.subTables.add(fromJson(key, it as JSONObject)) }
                        else -> table.columns.add(Column(key, obj.toString()))
                    }
                }
                tables.add(table)
            }
            return tables
        }
        fun fromJson(tableName: String, data: JSONObject): Table {
            val table = Table(tableName)

            data.keys().forEach { key ->
                when (val obj = data.get(key)) {
                    is JSONObject -> fromJson(key, obj)
                    is JSONArray -> obj.forEach { table.subTables.add(fromJson(key, it as JSONObject)) }
                    else -> table.columns.add(Column(key, obj.toString()))
                }

            }
            return table
        }
    }
}