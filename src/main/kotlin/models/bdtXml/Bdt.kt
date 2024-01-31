package models.bdtXml

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import com.gitlab.mvysny.konsumexml.konsumeXml
import models.bdtXml.actions.*
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import java.io.File

val globalElementsAccountedFor =
    setOf("Define", "DBQuery", "GetRSFieldValue", "Assign", "If", "ReplaceVariables", "CurrentRule", "InsertSection")

data class Bdt(
    val name: String,
    val primaryDataSource: String,
    val serverVer: String,
    val sequence: ArrayList<Action>,
) {

    fun getRevisionUnits(): ArrayList<String> {
        val results: ArrayList<String> = ArrayList()

        for (s in sequence) {
            if (s is Section) {
                if(s.revisionUnits.isNotEmpty()) {
                    results += s.revisionUnits
                }
            }
        }
        return results
    }

    fun solve(dataSource: DataSource, contentDb: ContentItemsDb): Pair<ArrayList<Action>, ArrayList<Action>> {
        val bdtSolver = BdtSolver(sequence, dataSource, contentDb)

        try {
            bdtSolver.go()
        } catch (e: Exception) {
            throw Exception("$name\n", e)
        }

        return bdtSolver.result()
    }

    companion object {
        private fun excludeXMLVersioningInfo(xmlString: String) : String {
            return xmlString.replace("\\<\\?xml(.+?)\\?\\>", "").trim();
        }

        fun fromFilePath(path: String) : Bdt {
            val xml = File(path).readText()
            return fromXmlString(xml)
        }
        fun fromXmlString(xmlString: String) : Bdt {
            val xml = excludeXMLVersioningInfo(xmlString)
            return xml.konsumeXml().child("BDT") { xml(this) }
        }
        fun xml(k: Konsumer): Bdt {
            k.checkCurrent("BDT")

            val name = k.attributes.getValue("name")
            val primaryDataSource = k.attributes.getValue("primaryDataSource")
            val serverVer = k.attributes.getValue("serverVer")
            val sequence: ArrayList<Action> = ArrayList()

            k.allChildrenAutoIgnore(Names.of(globalElementsAccountedFor)) {
                when (localName) {
                    "Define" -> sequence.add(Define.xml(this))
                    "DBQuery" -> sequence.add(DbQuery.xml(this))
                    "GetRSFieldValue" -> sequence.add(GetRSFieldValue.xml(this))
                    "Assign" -> sequence.add(Assignment.xml(this))
                    "If" -> sequence.add(If.xml(this))
                    "ReplaceVariables" -> sequence.add(ReplaceVariables.xml(this))
                    "CurrentRule" -> {
                        sequence.add(Rule.xml(this))
                    }

                    "InsertSection" -> {
                        sequence.add(Section.xml(this))
                    }
                }

            }
            return Bdt(
                name,
                primaryDataSource,
                serverVer,
                sequence
            )
        }
    }
}

