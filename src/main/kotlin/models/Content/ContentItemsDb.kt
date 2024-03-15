package models.Content

import api.DartClient
import api.models.NetworkResult
import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import models.bdtXml.Bdt
import org.json.JSONObject
import java.io.File
import kotlin.system.exitProcess

class ContentItemsDb(private val contentItems: ArrayList<ContentItem>) {
    fun getContentItem(name: String, state: String) : ContentItem? {
        val contentItem = contentItems.find { it.name.lowercase() == name.lowercase()}

        contentItem?.states?.forEach { _ ->
            if (contentItem.states.contains(state.lowercase())) {
                return contentItem
            }
        }
        return null
    }

    fun getContentItems() {
        contentItems.forEach {
            println(it)
        }
    }

    companion object {
        suspend fun fromNetwork(documentName: String) : ContentItemsDb {
            when(val content = DartClient.service.getContentItems(documentName)) {
                is NetworkResult.Success -> return ContentItemsDb(ArrayList(content.data.content))
                is NetworkResult.Error -> throw Exception(content.errorMsg)
                is NetworkResult.Exception -> throw Exception(content.e)
            }
        }

        fun fromCsv(path: String) : ContentItemsDb {
            val csvFile = File(path)
            return fromCsv(csvFile)
        }
        fun fromCsv(csvFile: File): ContentItemsDb {
            val contentItems: ArrayList<ContentItem> = ArrayList()

            csvReader().open(csvFile) {
                readAllAsSequence().forEachIndexed { index, row: List<String> ->
                    if(index != 0) {
                        val version = row[4]
                        val path = row[2].plus("_v$version.html")
                        val states = row.last().split(",").map { it.trim().lowercase() }

                       contentItems.add(ContentItem(row[2], path, states, version.toInt()))
                    }
                }
            }
            return ContentItemsDb(contentItems)

        }
    }
}