package api.models

data class StatesApiResponse(
    val BASE_VARIATION_INDICATOR: String,
    val EFFECTIVE_DATE: String,
    val LAST_MODIFIED_TIME: String,
    val SITUS_STATE: String,
    val STATUS: String,
    val WITHDRAW_DATE: String
)