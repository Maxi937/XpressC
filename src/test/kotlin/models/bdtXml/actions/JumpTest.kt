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


class JumpTest {
    @Test
    fun jump() {
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/Jump/JumpInSubdoc.xml")
        val bdtProvider = SubdocumentBdtProvider("src/test/resources/Actions/Jump/")
        val contentDb = ContentItemsDb.fromCsv("src/test/resources/ContentDb/GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table_Content_Items.csv")
        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")

        val solver = BdtSolver(bdt.sequence, dataSource, contentDb, bdtProvider)

        solver.go()

        val (_, sequence) = solver.result()

        val result = sequence.filter { it is InsertTextpiece && it.name == "SAAT_Table_Data_By_Benefit" }

        result.forEach {
            println(it)
        }

        assertEquals(3, result.count())
    }
}