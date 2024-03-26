package api.models

data class contentItemApiResponse(
    val AUTHOR: String,
    val CONTENT_GROUP_ID: String,
    val CONTENT_GROUP_NAME: String,
    val CONTENT_GROUP_TYPE: String,
    val CONTENT_ID: String,
    val DATA_ID: String,
    val FORMAT: String,
    val LAST_MODIFIED_TIME: String,
    val MAJOR_VERSION: String,
    val MINOR_VERSION: String,
    val MODIFIED_DATE: String,
    val NAME: String,
    val ORIGIN_ID: String,
    val PRODUCT_ID: String,
    val RULE_ID: String,
    val SHARED: String,
    val Sv_CONTENT_ID: String,
    val Sv_Notes_ID: String,
    val TEXTCLASS_ID: String,
    val TYPE: String,
    val USAGE_ID: String,
    val states: List<StatesApiResponse>
)