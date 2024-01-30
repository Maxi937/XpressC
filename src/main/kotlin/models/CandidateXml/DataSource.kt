package models.CandidateXml

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import java.io.File

// This data source model will probably only work for POSTX xml's a new one is needed for a non PostX xml
data class DataSource(
    private val tables: ArrayList<Table> = ArrayList()
) {

    fun getTable(tableName: String) : Table? {
        return tables.find { it.name.lowercase() == tableName.lowercase() }
    }

    fun find(tableName: String, columnName: String) : String? {
        val table = getTable(tableName)

        val column = table?.columns?.find { it.name.lowercase() == columnName.lowercase() }

        if (column != null) {
            return column.value
        }
        return null
    }

companion object {
    fun fromXmlString(xmlString: String): DataSource {
        val json = XML.toJSONObject(xmlString)
        return fromJson(json)
    }

    fun fromJson(json: JSONObject) : DataSource {
        var tables = ArrayList<Table>()

        json.keys().forEach { key ->
            val document = json.getJSONObject(key)
            tables = Table.jsonDataToTables(document)
        }
        return DataSource(tables)
    }
}

}
