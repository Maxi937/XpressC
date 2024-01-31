package models.CandidateXml

import exceptions.BdtDataSourceRecordException
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

    fun getRecordSet() : RecordSet {
        return recordSets[0]
    }

    fun getRecordSet(name: String) : RecordSet {
        val recordSet = recordSets.find { it.name.lowercase() == name.lowercase() }
        return recordSet ?: throw BdtDataSourceRecordException(name, this)
    }

    private fun createTablesFromJson(json: JSONObject) {
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
        createRecordSetsFromTables(tables)
    }

    private fun createRecordSetsFromTables(tables: ArrayList<Table>) {
        val tableNamesProcessed: ArrayList<String> = ArrayList()

        tables.forEach { table ->
            if(!tableNamesProcessed.contains(table.name)) {
                val tablesToAdd = tables.filter { it.name == table.name }
                recordSets.add(RecordSet(table.name, tablesToAdd))
                tableNamesProcessed.add(table.name)
            }
        }
    }

    companion object {

        fun fromFilePath(name: String, path: String) : DataSource {
            val xml = File(path).readText()
            return fromXmlString(name, xml)
        }
        fun fromXmlString(name: String, xmlString: String): DataSource {
            val json = XML.toJSONObject(xmlString)
            return fromJson(name, json)
        }

        fun fromJson(name: String, json: JSONObject): DataSource {
            val dataSource = DataSource(name, ArrayList())

            json.keys().forEach { key ->
                val document = json.getJSONObject(key)
                dataSource.createTablesFromJson(document)
            }

            return dataSource
        }
    }

}
