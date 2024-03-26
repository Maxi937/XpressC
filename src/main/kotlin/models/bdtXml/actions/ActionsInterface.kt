package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.bdtsolver.BdtSolver
import models.bdtXml.containers.If
import models.bdtXml.containers.Section
import models.bdtXml.containers.SubDocument
import org.json.JSONObject


interface Action {
    var sequenceId: Int
    fun evaluate(bdtSolver: BdtSolver)

    fun toJson(): JSONObject

    fun setup(bdtSolver: BdtSolver) {}
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
        "GetUserExitValue" -> GetUserExit.xml(k)
        "DBQuery" -> DbQuery.xml(k)
        else -> {
            null
        }
    }
}