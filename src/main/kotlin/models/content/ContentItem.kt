package models.content

data class ContentItem(
    val name: String,
    val path: String,
    val states: List<String> = ArrayList(),
    val version: Int
) {

    companion object {
        fun fromHtmlString() {

        }
    }
}
