package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver

// RecordSetVar sets the current active DB Table, Commonly can be seen as part of a READ which is expressed in the
// BDT as a DbQuery. A RecordSetVar will always happen before a field from a Db is read, the field must be in
// the record set or this is an exception.

data class RecordSetVar(
    val name: String,
) : Action {
    companion object {
        fun xml(k: Konsumer): RecordSetVar {
            k.checkCurrent("RecordsetVar")
            val name = k.attributes.getValue("name")
            return RecordSetVar(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.setRecordSet(name)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        return sequence
    }

}