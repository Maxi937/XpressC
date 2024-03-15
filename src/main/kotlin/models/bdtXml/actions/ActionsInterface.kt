package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.BdtSolver
import org.json.JSONObject


interface Action {
    fun evaluate(bdtSolver: BdtSolver)

    fun gather(sequence: ArrayList<Action>) : ArrayList<Action>

    fun toJson(): JSONObject
}

fun whichAction(k: Konsumer): Action? {
    return when (k.localName) {
        "Block" -> k.childOrNull(Names.any()) { whichAction(this) }
        "CurrentRule" -> Rule.xml(k)
        "ReplaceVariables" -> ReplaceVariables.xml(k)
        "GetRSFieldValue" -> GetRSFieldValue.xml(k)
        "Reset" -> Reset.xml(k)
        "Assign" -> Assignment.xml(k)
        "If" -> If.xml(k)
        "CRQuery" -> CrQuery.xml(k)
        "InsertTextpiece" -> InsertTextpiece.xml(k)
        "InsertSection" -> Section.xml(k)
        "RecordsetMoveNext" -> RecordSetMoveNext.xml(k)
        "SubDocument" -> SubDocument.xml(k)
        "Label" -> Label.xml(k)
        "Jump" -> Jump.xml(k)
        else -> {
            null
        }
    }
}