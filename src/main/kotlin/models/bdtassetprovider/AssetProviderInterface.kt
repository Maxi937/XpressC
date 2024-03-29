package models.bdtassetprovider

import models.Content.ContentGroup
import models.bdtXml.Bdt

interface AssetProviderInterface {
    fun getBdt(documentId: Long): Bdt

    fun getBdt(documentName: String): Bdt

    fun getContentGroup(documentName: String, textClassId: Int): ContentGroup
}
