package models.bdtXml.actions

import models.bdtXml.Bdt
import models.bdtassetprovider.LocalAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.io.path.Path

class RecordSetTest {
    @Test
    fun recordSetTest() {
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/RecordSetTest"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/RecordSetTest/RecordSetTest.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_CA.xml")
        val result = bdt.compile(dataSource, assetProvider)
    }
}