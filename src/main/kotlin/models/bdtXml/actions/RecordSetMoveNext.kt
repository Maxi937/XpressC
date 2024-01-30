package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.BdtSolver


data class RecordSetMoveNext(
    val recordSetVar: RecordSetVar
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
        bdtSolver.setRecordSet(recordSetVar.name)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
//        sequence.add(this)
        return sequence
    }

}