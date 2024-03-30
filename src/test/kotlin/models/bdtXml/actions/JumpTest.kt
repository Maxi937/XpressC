package models.bdtXml.actions


import models.bdtXml.Bdt
import models.bdtassetprovider.LocalAssetProvider
import models.bdtassetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals


class JumpTest {
    @Test
    fun jump() {
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/Jump"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/Jump/JumpInSubdoc.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/6_ADF_Classes.xml")

        val spacerContentName = "SAAT_Table_Data_By_Benefit_Spacer"
        val contentItemName = "SAAT_Table_Data_By_Benefit"
        val contentItems = bdt.compile(dataSource, assetProvider).contentItems

        contentItems.forEach {
            println(it)
        }

        assertEquals(8, contentItems.count())

        assert(contentItems[0].name == contentItemName)
        assert(contentItems[1].name == contentItemName)
        assert(contentItems[2].name == contentItemName)
        assert(contentItems[3].name == spacerContentName)
        assert(contentItems[4].name == contentItemName)
        assert(contentItems[5].name == contentItemName)
        assert(contentItems[6].name == contentItemName)
        assert(contentItems[7].name == spacerContentName)
    }

    @Test
    fun realWorldScenaio() {
        val assetProvider = NetworkAssetProvider("dev")
        val bdt: Bdt =
            Bdt.fromFilePath("C:\\Users\\YK09\\Development\\Projects\\dart\\dart-node-app\\server\\jobs\\compile\\06e9e835-48b7-4a72-a2d6-77169c42e2ef\\bdt.xml")
        val dataSource =
            DataSource.fromFilePath("C:\\Users\\YK09\\Development\\Projects\\dart\\dart-node-app\\server\\jobs\\compile\\06e9e835-48b7-4a72-a2d6-77169c42e2ef\\candidate.xml")

        val result = bdt.compile(dataSource, assetProvider)

        result.contentItems.forEach {
            println(it)
        }
    }
}