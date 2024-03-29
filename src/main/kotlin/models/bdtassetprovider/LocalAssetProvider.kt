package models.bdtassetprovider

import models.Content.ContentGroup
import models.Content.ContentItem
import models.bdtXml.Bdt
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