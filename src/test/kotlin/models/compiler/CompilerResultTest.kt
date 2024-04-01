package models.compiler

import models.Bdt
import models.assetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

class CompilerResultTest {

    private val assetProvider = NetworkAssetProvider("dev")

    @Test
    fun toJson() {
        val bdt: Bdt = Bdt.fromFilePath("src/test/resources/RealJobExample/bdt.xml")
        val dataSource = DataSource.fromFilePath("src/test/resources/RealJobExample/candidate.xml")

        val result = bdt.compile(dataSource, assetProvider)

        val json = result.toJson()
        println(json)
    }
}