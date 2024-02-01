package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.BdtSolver

interface Condition {
    fun evaluate(bdtSolver: BdtSolver) : Boolean
}

fun whichCondition(k: Konsumer) : Condition? {
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