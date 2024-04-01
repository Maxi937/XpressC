package models.assetprovider

import interfaces.AssetProviderInterface
import models.contentx.ContentGroup
import models.contentx.ContentItem
import models.Bdt
import java.nio.file.Path

class LocalAssetProvider(private val dir: Path) : AssetProviderInterface {

    override fun getBdt(documentId: Long): Bdt {
        return Bdt.fromFilePath(dir.resolve("${documentId}.xml").toString())
    }

    override fun getBdt(documentName: String): Bdt {
        return Bdt.fromFilePath(dir.resolve(documentName).toString())
    }

    override fun getContentGroup(documentName: String, textClassId: Int): ContentGroup {
        val contentItem = ContentItem("dummy", "path", listOf("AK", "CA"), 1)
        return ContentGroup(12345, 12345, "dummyContentGroup", "fake", listOf())
    }
}