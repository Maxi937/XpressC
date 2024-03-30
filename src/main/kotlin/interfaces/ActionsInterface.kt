package interfaces

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.syntax.actions.*
import models.compiler.Compiler
import models.syntax.containers.If
import models.syntax.containers.Section
import models.syntax.containers.SubDocument
import org.json.JSONObject
import java.util.*


interface Action {

    val uuid: UUID

    fun evaluate(compiler: Compiler): Boolean

    fun toJson(): JSONObject

    fun setup(compiler: Compiler) {}

    fun copy(): Action
}

fun whichAction(k: Konsumer): Action? {
    return when (k.localName) {
        "Block" -> k.childOrNull(Names.any()) { whichAction(this) }
        "Define" -> Define.xml(k)
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
        "UCD" -> {
            k.skipContents()
            return null
        }

        else -> {
            return null
        }
    }
}