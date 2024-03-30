//package models.bdtXml.actions
//
//import models.Content.ContentItemsDb
//import models.Bdt
//import models.bdtassetprovider.BdtAssetProvider
//import models.datasource.DataSource
//import org.junit.jupiter.api.Test
//import utils.SubdocumentBdtProvider
//import kotlin.test.assertEquals
//import kotlin.test.assertIs
//import kotlin.test.assertNotNull
//
//class SubdocumentTest {
//
//    val assetProvider = BdtAssetProvider("dev")
//
//    val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
//    private val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Actions/Subdocument/OneSubdocument/Main.xml")
//
//    @Test
//    fun oneSubdocument() {
//        val dataSource =
//            DataSource.fromFilePath(bdt.primaryDataSource, "src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
//        val (_, sequence) = bdt.solve(dataSource, contentDb, bdtProvider)
//
//        val action = sequence.last()
//
//        sequence.forEach {
//            println(it)
//        }
//
//        assertIs<InsertTextpiece>(action, "InsertTextPiece not triggered")
//        assertNotNull(action.contentItem, "InsertTextPiece Content Item null")
//        assertEquals(action.contentItem!!.name, "SAAT_Table_Data_By_Benefit_Spacer")
//    }
//}