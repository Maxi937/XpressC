package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.BdtSolver

interface Condition {
    fun evaluate(bdtSolver: BdtSolver) : Boolean
}

fun whichCondition(k: Konsumer) : Condition? {
    return when (k.localName) {
        "Condition" -> k.child(Names.any()) { whichCondition(this) }
        "VariableTest" -> VariableTest.xml(k)
        "Comparison" -> Comparison.xml(k)
        "RecordsetTest" -> RecordSetTest.xml(k)
        "And" -> And.xml(k)
        "Or" -> Or.xml(k)
        else -> {
            null
        }
    }
}