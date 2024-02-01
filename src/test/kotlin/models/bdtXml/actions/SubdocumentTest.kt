package models.bdtXml.actions

import Actions.Subdocument.SubdocumentBdtProvider
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertIsNot
import kotlin.test.assertNotNull

class SubdocumentTest {
    private val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/Subdocument/OneSubdocument/Main.xml")
    private val bdtProvider = SubdocumentBdtProvider("src/test/resources/Actions/Subdocument/OneSubdocument")
    private val contentDb = ContentItemsDb.fromCsv("src/test/resources/ContentDb/GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table_Content_Items.csv")

    @Test
    fun oneSubdocument() {
        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val (_, sequence) = bdt.solve(dataSource, contentDb, bdtProvider)

        val action = sequence.last()

        sequence.forEach {
            println(it)
        }

        assertIs<InsertTextpiece>(action, "InsertTextPiece not triggered")
        assertNotNull(action.contentItem, "InsertTextPiece Content Item null")
        assertEquals(action.contentItem!!.name, "SAAT_Table_Data_By_Benefit_Spacer")
    }
}