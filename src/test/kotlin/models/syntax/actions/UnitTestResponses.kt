package models.syntax.actions

import models.Bdt
import models.assetprovider.LocalAssetProvider
import models.datasource.DataSource
import models.unittest.RevisionUnitTest
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals

class UnitTestResponses {

    @Test
    fun revisionUnitsPass() {
        println("Revision Units Test Pass\n")
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/UnitTests/RevisionUnits"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/UnitTests/RevisionUnits/RevisionUnitPass.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/6_ADF_Classes.xml")

        val result = bdt.compile(dataSource, assetProvider)


        val tests = result.tests
        val revTest = tests.filterIsInstance<RevisionUnitTest>()

        revTest[0].result.toSet().forEach {
            println(it)
        }

        assertEquals(1, revTest.count(),)
        assertEquals(true, revTest[0].pass)

        println("\n")
    }

    @Test
    fun revisionUnitsFail() {
        println("Revision Unit Test Fail\n")
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/UnitTests/RevisionUnits"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/UnitTests/RevisionUnits/RevisionUnitFail.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/6_ADF_Classes.xml")

        val result = bdt.compile(dataSource, assetProvider)

        val tests = result.tests
        val revTest = tests.filterIsInstance<RevisionUnitTest>()

        revTest[0].result.toSet().forEach {
            println(it)
        }

        assertEquals(1, revTest.count(),)
        assertEquals( false, revTest[0].pass,)

        println("\n")
    }


}