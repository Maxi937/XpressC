package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.BdtSolver
import models.bdtXml.ObjectRefListVar

data class Reset(
    val objectRefListVar: ObjectRefListVar
) : Action {
    companion object {
        fun xml(k: Konsumer): Reset {
            k.checkCurrent("Reset")

            var objectRefListVar: ObjectRefListVar? = null

            k.child("ObjectRefListVar") {
                objectRefListVar = ObjectRefListVar.xml(this)
            }

            return Reset(objectRefListVar!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
//        return true
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        return sequence
    }
}