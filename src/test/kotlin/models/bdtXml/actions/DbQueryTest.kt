package models.bdtXml.actions

import models.BdtSolver
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import org.junit.jupiter.api.Test
import utils.SubdocumentBdtProvider
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNotNull

// TODO: DB query needs to be refactored to read and set the active record set similar to how an
class DbQueryTest {
    @Test
    fun dbQueryValid() {
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/DbQuery/DbQuery.xml")
        val bdtProvider = SubdocumentBdtProvider("")
        val contentDb =
            ContentItemsDb.fromCsv("src/test/resources/ContentDb/GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table_Content_Items.csv")

        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val bdtSolver = BdtSolver(bdt.sequence, dataSource, contentDb, bdtProvider)

        bdtSolver.go()
        val (_, sequence) = bdtSolver.result()
        println(bdtSolver.activeRecordSet)
    }

    @Test
    fun dbQueryMultipleTables() {
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/DbQuery/DbQueryMultiTables.xml")
        val bdtProvider = SubdocumentBdtProvider("")
        val contentDb = ContentItemsDb.fromCsv("src/test/resources/ContentDb/GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table_Content_Items.csv")
        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val bdtSolver = BdtSolver(bdt.sequence, dataSource, contentDb, bdtProvider)

        bdtSolver.go()
        val (_, sequence) = bdtSolver.result()
        bdtSolver.dataSource.recordSets.forEach {
            println(it)
        }
    }
}