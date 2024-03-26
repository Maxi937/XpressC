package models.datasource

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import java.io.File

// This data source model will probably only work for POSTX xml's a new one is needed for a non PostX xml
data class DataSource(
    val name: String,
    val tables: ArrayList<Table>,
    val recordSets: ArrayList<RecordSet> = ArrayList(),
) {
    init {
        recordSets.add(RecordSet(tables[0].name, listOf(tables[0])))
    }

    fun getRecordSet(name: String): RecordSet? {
        return recordSets.find { it.name.lowercase() == name.lowercase() }
    }

    private fun executeQuery(tableName: String, tables: ArrayList<Table>, query: Query): List<Table> {
        return tables.filter { it.getField(query.columnName) == query.value && it.name.lowercase() == tableName.lowercase() }
    }

    fun query(tableName: String, queries: ArrayList<Query>): RecordSet {
        var result = executeQuery(tableName, tables, queries[0])

        queries.forEachIndexed { indx, element ->
            if (indx != 0) {
                result = executeQuery(tableName, ArrayList(result), element)
            }
        }

        result.addLast(Table("EOF"))

        val recordSet = getRecordSet(tableName)

        if (recordSet != null) {
            recordSet.data = result
            recordSet.activeRecordSet = recordSet.data[0]
            return recordSet
        }

        recordSets.add(RecordSet(tableName, result))
        return recordSets.last()
    }

//    private fun createRecordSetsFromTables(tables: ArrayList<Table>) {
//        val tableNamesProcessed: ArrayList<String> = ArrayList()
//
//        tables.forEach { table ->
//            if(!tableNamesProcessed.contains(table.name)) {
//                val tablesToAdd = tables.filter { it.name == table.name }
//                tablesToAdd.addFirst(Table("EOF", ArrayList()))
//                recordSets.add(RecordSet(table.name, tablesToAdd.reversed()))
//                tableNamesProcessed.add(table.name)
//            }
//        }
//    }

    companion object {
        fun fromFilePath(path: String): DataSource {
            val xml = File(path).readText()
            return fromXmlString(path, xml)
        }

        fun fromXmlString(name: String, xmlString: String): DataSource {
            val json = XML.toJSONObject(xmlString)
            return fromJson(name, json)
        }

        fun fromJson(name: String, json: JSONObject): DataSource {
            var tables = ArrayList<Table>()

            json.keys().forEach { key ->
                val document = json.getJSONObject(key)
                tables = createTablesFromJson(document)
            }

            return DataSource(name, tables)
        }

        private fun createTablesFromJson(json: JSONObject): ArrayList<Table> {
            val tables = ArrayList<Table>()

            json.keys().forEach { property ->
                val table = Table(property.toString())
                val data = json.getJSONObject(table.name)

                data.keys().forEach { key ->
                    when (val obj = data.get(key)) {
                        is JSONObject -> tables += Table.fromJson(key, obj)
                        is JSONArray -> obj.forEach { tables += Table.fromJson(key, it as JSONObject) }
                        else -> table.columns.add(Column(key, obj.toString()))
                    }
                }
                tables.add(table)
            }
            tables.reverse()
            return tables
        }
    }

}
