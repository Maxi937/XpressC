package models.bdtXml.actions

import models.bdtXml.Bdt
import models.bdtassetprovider.NetworkAssetProvider
import models.datasource.DataSource
import models.datasource.Query
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

// TODO: DB query needs to be refactored to read and set the active record set similar to how an
class DbQueryTest {
    @Test
    fun dbQueryValid() {
        val assetProvider = NetworkAssetProvider("dev")
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/DbQuery/DbQueryMultiTables.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        bdt.compile(dataSource, assetProvider)
        val queries = ArrayList<Query>()

        queries.add(Query("CONTRACTPK", "1", "eq"))

        val result = dataSource.query("Contract", queries)

        assertEquals(2, result.data.size)
    }

    @Test
    fun dbQueryMultiple() {
        val assetProvider = NetworkAssetProvider("dev")
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/DbQuery/DbQueryMultiTables.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_CA.xml")
        bdt.compile(dataSource, assetProvider)
        val queries = ArrayList<Query>()

        queries.add(Query("TYPE", "PDP", "eq"))
        queries.add(Query("CONTRACTFK", "1", "eq"))

        val result = dataSource.query("ADF", queries)

        result.data.forEach {
            println(it)
        }

        assertEquals(3, result.data.size)
    }
}