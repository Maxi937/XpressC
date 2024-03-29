package models.bdtXml

import api.DartClient
import api.models.NetworkResult
import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.konsumeXml
import models.bdtXml.actions.whichAction
import models.bdtXml.compiler.Compiler
import models.bdtXml.compiler.CompilerResult
import models.bdtXml.containers.Section
import models.bdtassetprovider.AssetProviderInterface
import models.datasource.DataSource
import org.json.JSONArray
import java.io.File

data class Bdt(
    val name: String,
    val primaryDataSource: String,
    val serverVer: String,
    val sequence: Sequence,
) {
    fun toJson(): JSONArray {
        return sequence.toJson()
    }

    fun getRevisionUnits(): ArrayList<String> {
        val results: ArrayList<String> = ArrayList()
        sequence.execute { it ->
            if (it is Section) {
                results += it.revisionUnits
            }
        }
        return results
    }

    fun compile(dataSource: DataSource, assetProvider: AssetProviderInterface): CompilerResult {
        val compiler = Compiler(sequence, dataSource, assetProvider)
        return compiler.compile()
    }


    companion object {
        private fun excludeXMLVersioningInfo(xmlString: String): String {
            return xmlString.replace("\\<\\?xml(.+?)\\?\\>", "").trim();
        }

        private fun excludeComments(xmlString: String): String {
            return xmlString.replace("<!--[\\s\\S]*?-->", "");
        }


        suspend fun fromNetwork(documentName: String, env: String): Bdt {
            when (val bdt = DartClient.service.getBdtXml(documentName, env)) {
                is NetworkResult.Success -> return fromXmlString(bdt.data.bdtXml)
                is NetworkResult.Error -> throw Exception(bdt.errorMsg)
                is NetworkResult.Exception -> throw Exception(bdt.e)
            }
        }

        suspend fun fromNetwork(documentId: Long, env: String): Bdt {
            when (val bdt = DartClient.service.getBdtXml(documentId, env)) {
                is NetworkResult.Success -> return fromXmlString(bdt.data.bdtXml)
                is NetworkResult.Error -> throw Exception(bdt.errorMsg)
                is NetworkResult.Exception -> throw Exception(bdt.e)
            }
        }

        fun fromFilePath(path: String): Bdt {
            val xml = File(path).readText()
            return fromXmlString(xml)
        }

        fun fromXmlString(xmlString: String): Bdt {
            val noComments = excludeComments(xmlString)
            val noVersioning = excludeXMLVersioningInfo(noComments)
            return noVersioning.konsumeXml().child("BDT") { xml(this) }
        }

        fun xml(k: Konsumer): Bdt {
            k.checkCurrent("BDT")

            val name = k.attributes.getValue("name")
            val primaryDataSource = k.attributes.getValue("primaryDataSource")
            val serverVer = k.attributes.getValue("serverVer")
            val sequence: Sequence = Sequence()

            k.children(Names.any()) {
                val action = whichAction(this)

                if (action != null) {
                    sequence.add(action)
                }
            }

            return Bdt(
                name,
                primaryDataSource,
                serverVer,
                sequence,
            )
        }
    }
}

