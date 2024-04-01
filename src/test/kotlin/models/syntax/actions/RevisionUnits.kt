package models.syntax.actions

import models.Bdt
import models.assetprovider.LocalAssetProvider
import models.assetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test
import kotlin.io.path.Path
import kotlin.test.assertEquals

class RevisionUnits {
    @Test
    fun revisionUnits() {
        println("Revision Units Test\n")
        val assetProvider = LocalAssetProvider(Path("src/test/resources/Bdt/RevisionUnits"))
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/Bdt/RevisionUnits/JumpInSubdoc.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/Candidate/6_ADF_Classes.xml")

        val result = bdt.compile(dataSource, assetProvider)

        val revisionUnits = result.getRevisionUnits()

        revisionUnits.forEach {
            println(it)
        }

        assertEquals("SL23_SAAT_Table_Data_By_Benefit_Medical", revisionUnits[0])
        assertEquals("SL23_SAAT_Table_Data_By_Benefit_PDP", revisionUnits[1])


        println("\n")
    }


}