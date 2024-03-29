package models.bdtXml.actions

import models.bdtXml.Bdt
import models.bdtassetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertIsNot


class IfTest {
    private val assetProvider = NetworkAssetProvider("dev")
    private val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/If/If.xml")

    @Test
    fun conditionSatisfied() {
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val result = bdt.compile(dataSource, assetProvider)

        val action = result.eventSequence.last()

        println("Action: $action")
        assertIs<InsertTextpiece>(action, "InsertTextPiece not triggered")
        assertEquals(action.name, "Stop_Loss_2023SL_Header_Footer_Roman_Numbering")
    }

    @Test
    fun conditionNotSatisfied() {
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_CA.xml")
        val result = bdt.compile(dataSource, assetProvider)

        val action = result.eventSequence.last()
        println("Action: $action")
        assertIsNot<InsertTextpiece>(action, "InsertTextPiece is the last action in the sequence")
    }
}