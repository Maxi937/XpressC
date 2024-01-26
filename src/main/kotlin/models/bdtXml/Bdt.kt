package models.bdtXml

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import com.gitlab.mvysny.konsumexml.konsumeXml
import models.bdtXml.actions.*
import models.CandidateXml.Candidate

val globalElementsAccountedFor =
    setOf("Define", "DBQuery", "GetRSFieldValue", "Assign", "If", "ReplaceVariables", "CurrentRule", "InsertSection")

data class BDT(
    val name: String,
    val primaryDataSource: String,
    val serverVer: String,
    val sequence: List<Action>
) {
    fun getRevisionUnits(): ArrayList<String> {
        val results: ArrayList<String> = ArrayList()

        for (s in sequence) {
            if (s is Section) {
                if(s.revisionUnit != null) {
                    results.add(s.revisionUnit)
                }
            }
        }
        return results
    }

    fun solve(candidateXml: Candidate) {
        sequence.forEach {
            it.evaluate()
        }

    }


    companion object {
        fun fromXmlString(xmlString: String) : BDT {
            return xmlString.konsumeXml().child("BDT") { xml(this) }
        }
        fun xml(k: Konsumer): BDT {
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
            return BDT(
                name,
                primaryDataSource,
                serverVer,
                sequence
            )
        }
    }
}

