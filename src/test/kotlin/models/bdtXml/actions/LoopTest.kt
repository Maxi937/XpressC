package models.bdtXml.actions


import models.bdtXml.Bdt
import models.bdtassetprovider.LocalAssetProvider
import models.bdtassetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals


class LoopTest {
    @Test
    fun loop() {
        println("Loop Test\n")
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/Loop"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/Loop/JumpInSubdoc.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/6_ADF_Classes.xml")

        val spacer = "SAAT_Table_Data_By_Benefit_Spacer"
        val tableData = "SAAT_Table_Data_By_Benefit"
        val contentItems = bdt.compile(dataSource, assetProvider).contentItems

        contentItems.forEach {
            println(it)
        }

        assertEquals(8, contentItems.count())

        assert(contentItems[0].name == tableData)
        assert(contentItems[1].name == tableData)
        assert(contentItems[2].name == tableData)
        assert(contentItems[3].name == spacer)
        assert(contentItems[4].name == tableData)
        assert(contentItems[5].name == tableData)
        assert(contentItems[6].name == tableData)
        assert(contentItems[7].name == spacer)

        println("\n")
    }

    /**
     * This test has a dependency on a build of the dart application being live on the server as it
     * uses Network Requests to pull in any Subdocuments requested by the Bdt. The Example Bdt in this scenario is a full
     * contract pulling in many Subdocuments.
     */
    @Test
    fun jobExample() {
        println("Job Example Test\n")
        val assetProvider = NetworkAssetProvider("dev")
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/RealJobExample/bdt.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/RealJobExample/candidate.xml")

        val result = bdt.compile(dataSource, assetProvider)

        val spacer = "SAAT_Table_Data_By_Benefit_Spacer"
        val tableData = "SAAT_Table_Data_By_Benefit"

        val contentItemsApplicable = result.contentItems.filter {
            it.name == tableData || it.name == spacer
        }

        contentItemsApplicable.forEach {
            println(it)
        }

        assertEquals(10, contentItemsApplicable.count())
        assert(contentItemsApplicable[0].name == tableData)
        assert(contentItemsApplicable[1].name == tableData)
        assert(contentItemsApplicable[2].name == tableData)
        assert(contentItemsApplicable[3].name == tableData)
        assert(contentItemsApplicable[4].name == tableData)
        assert(contentItemsApplicable[5].name == spacer)
        assert(contentItemsApplicable[6].name == tableData)
        assert(contentItemsApplicable[7].name == tableData)
        assert(contentItemsApplicable[8].name == tableData)
        assert(contentItemsApplicable[9].name == spacer)

        println("\n")
    }
}