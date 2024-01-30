package models.Content

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import java.io.File
import kotlin.system.exitProcess

class ContentItemsDb(private val contentItems: ArrayList<ContentItem>) {

    fun getContentItem(name: String, state: String) : ContentItem? {
        val contentItem = contentItems.find { it.name.lowercase() == name.lowercase()}

        contentItem?.states?.forEach { _ ->
            if (contentItem.states.contains(state)) {
                return contentItem
            }
        }
        return null
    }

    companion object {
        fun fromCsv(csvFile: File): ContentItemsDb {
            val contentItems: ArrayList<ContentItem> = ArrayList()

            csvReader().open(csvFile) {
                readAllAsSequence().forEachIndexed { index, row: List<String> ->
                    if(index != 0) {
                        val version = row[4]
                        val path = row[2].plus("_v$version.html")
                        val states = row.last().split(",").map { it.trim() }
                       contentItems.add(ContentItem(row[2], path, states, version.toInt()))
                    }
                }
            }

//            contentItems.forEach { content ->
//               val filtered = contentItems.filter { it.name == content.name }
//                filtered.forEach {f ->
//                    println(f.name)
//                }
//                println("\n")
//            }

            return ContentItemsDb(contentItems)

        }
    }
}