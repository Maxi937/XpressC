package api.models

import models.Content.ContentItem

data class ContentItemsApiResponse(
    val content: List<ContentItem>,
)