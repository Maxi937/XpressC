package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject


data class RecordSetMoveNext(
    val recordSetVar: RecordSetVar,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): RecordSetMoveNext {
            k.checkCurrent("RecordsetMoveNext")

            var recordSetVar: RecordSetVar? = null

            k.child("RecordsetVar") {
                recordSetVar = RecordSetVar.xml(this)

            }

            return RecordSetMoveNext(recordSetVar!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        bdtSolver.addActionToSequence(this)
        bdtSolver.recordSetMoveNext()

    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}