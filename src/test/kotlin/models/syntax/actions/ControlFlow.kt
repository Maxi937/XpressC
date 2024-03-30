package models.syntax.actions

import models.Bdt
import models.assetprovider.LocalAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals

class ControlFlow {
    @Test
    fun controlFlowFail() {
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/ControlFlow"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/ControlFlow/ControlFlow.xml")

        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_CA.xml")
        val result = bdt.compile(dataSource, assetProvider)

        val v = result.runtimeVariables.find { it.name == "RESULT" }

        assert(v != null)
        assertEquals("fail", v!!.value)
    }

    @Test
    fun controlFlowPass() {
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/ControlFlow"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/ControlFlow/ControlFlow.xml")

        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml")
        val result = bdt.compile(dataSource, assetProvider)
        val v = result.runtimeVariables.find { it.name == "RESULT" }

        assert(v != null)
        assertEquals("pass", v!!.value)
    }
}