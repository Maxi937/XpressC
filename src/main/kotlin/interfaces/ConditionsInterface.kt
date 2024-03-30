package interfaces

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.syntax.conditions.*
import models.compiler.Compiler
import org.json.JSONObject

interface Condition {
    fun evaluate(bdtSolver: Compiler): Boolean

    fun toJson(): JSONObject

}

fun whichCondition(k: Konsumer): Condition? {
    return when (k.localName) {
        "WhereCondition" -> k.child(Names.any()) { whichCondition(this) }
        "Condition" -> k.child(Names.any()) { whichCondition(this) }
        "VariableTest" -> VariableTest.xml(k)
        "Comparison" -> Comparison.xml(k)
        "And" -> And.xml(k)
        "Or" -> Or.xml(k)
        "RecordsetTest" -> RecordSetTest.xml(k)
        else -> {
            null
        }
    }
}