package interfaces

import models.contentx.ContentGroup
import models.Bdt

interface AssetProviderInterface {
    fun getBdt(documentId: Long): Bdt

    fun getBdt(documentName: String): Bdt

    fun getContentGroup(documentName: String, textClassId: Int): ContentGroup
}
