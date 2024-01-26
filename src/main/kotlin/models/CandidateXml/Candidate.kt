package models.CandidateXml

import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import java.io.File

data class Candidate(
    val elements: JSONObject
) {
    /**
     * @param k
     * @return
     * This function will return the value of the first match of k
     */
    fun getValueFromJsonObject(k: String): String? {
        return recursiveSearch(k, elements)
    }

    /**
     * @param section
     * The Section to search in.
     * @param k
     * The Key to return the value from.
     * @return
     * This function will return the value of the first match of k in the given section.
     */
    fun getValueFromJsonObject(section: String, k: String): String? {
        return recursiveSearchSection(section, k, elements)
    }

    private fun recursiveSearch(k: String, json: JSONObject): String? {
        var result: String? = null
        val iter: Iterator<String> = json.keys()

        while (iter.hasNext() && result == null) {
            val key = iter.next()

            if (json.has(k)) {
                result = json.getString(k)
            } else {
                val obj = json[key]

                if (obj.javaClass == JSONObject::class.java) {
                    result = recursiveSearch(k, obj as JSONObject)
                }

                if (obj.javaClass == JSONArray::class.java) {
                    println("Arrray search not implemented")
                    println(obj)
                }
            }
        }
        return result
    }

    private fun recursiveSearchSection(section: String, k: String, json: JSONObject): String? {
        var result: String? = null

        val iter: Iterator<String> = json.keys()
        while (iter.hasNext() && result == null) {
            val key = iter.next()

            if (json.has(section)) {
                val sec = json.getJSONObject(section)
                result = sec.getString(k)
            } else {
                val obj = json[key]

                if (obj.javaClass == JSONObject::class.java) {
                    result = recursiveSearchSection(section, k, obj as JSONObject)
                }

                if (obj.javaClass == JSONArray::class.java) {
                    println("Arrray search not implemented")
                }
            }

        }
        return result
    }

    companion object {
        fun fromFile(file: File): Candidate {
            val elements = XML.toJSONObject(file.readText())
            return Candidate(elements)
        }

        fun fromXmlString(xmlString: String): Candidate {
            val elements = XML.toJSONObject(xmlString)
            return Candidate(elements)
        }
    }

}
