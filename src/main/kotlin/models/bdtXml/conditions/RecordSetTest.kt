package models.bdtXml.conditions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.actions.RecordSetVar
import models.bdtXml.compiler.Compiler
import org.json.JSONObject

data class RecordSetTest(
    val operator: String,
    val recordSetVar: RecordSetVar,
) : Condition {
    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun evaluate(bdtSolver: Compiler): Boolean {
        when (operator) {
            "noteod" -> {
                return bdtSolver.isNotEod(recordSetVar.name)
            }

            "eod" -> return bdtSolver.isEod(recordSetVar.name)
        }
        return false
    }

    companion object {
        fun xml(k: Konsumer): RecordSetTest {
            k.checkCurrent("RecordsetTest")

            val operator = k.attributes.getValue("operator")
            val recordSetVar: RecordSetVar = k.child("RecordsetVar") {
                return@child RecordSetVar.xml(this)
            }

            return RecordSetTest(operator, recordSetVar)
        }
    }


}