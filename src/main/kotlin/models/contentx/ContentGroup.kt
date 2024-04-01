package models.contentx

import api.models.contentItemApiResponse
import com.google.gson.annotations.SerializedName

data class ContentGroup(
    @SerializedName("CONTENT_GROUP_ID")
    val contentGroupId: Long,
    @SerializedName("RULE_ID")
    val ruleId: Long,
    @SerializedName("CONTENT_GROUP_NAME")
    val name: String,
    @SerializedName("STATUS")
    val status: String,
    @SerializedName("CONTENT_ITEMS")
    val contentItems: List<contentItemApiResponse>
) {
    fun getContentItem(state: String): contentItemApiResponse? {
        val contentItemsByState = this.contentItems.filter { content ->
            val c = content.states.find {
                it.SITUS_STATE == state
            }
            c != null
        }

        if (contentItemsByState.isEmpty()) {
            return null
        }

        if (contentItemsByState.size == 1) {
            return contentItemsByState[0]
        }

        var contentItem = contentItemsByState[0]

        contentItemsByState.forEach {
            if (it.MAJOR_VERSION > contentItem.MAJOR_VERSION) {
                contentItem = it
            }
        }
        return contentItem
    }
}
