package models.bdtXml.actions

import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import org.junit.jupiter.api.Test
import utils.SubdocumentBdtProvider
import kotlin.test.*


class IfTest {
    private val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/If/If.xml")
    private val bdtProvider = SubdocumentBdtProvider("src/test/resources/Actions/")
    private val contentDb = ContentItemsDb.fromCsv("src/test/resources/ContentDb/GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table_Content_Items.csv")

    @Test
    fun conditionSatisfied() {
        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val (_, sequence) = bdt.solve(dataSource, contentDb, bdtProvider)

        val action = sequence.last()
        println("Action: $action")
        assertIs<InsertTextpiece>(action, "InsertTextPiece not triggered")
        assertNotNull(action.contentItem, "InsertTextPiece Content Item null")
        assertEquals(action.contentItem!!.name, "SAAT_Table_Data_By_Benefit_Spacer")
    }

    @Test
    fun conditionNotSatisfied() {
        val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_CA.xml")
        val (_, sequence) = bdt.solve(dataSource, contentDb, bdtProvider)

        val action = sequence.last()
        println("Action: $action")
        assertIsNot<InsertTextpiece>(action, "InsertTextPiece is the last action in the sequence")
    }
}