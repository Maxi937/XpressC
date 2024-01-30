package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.BdtSolver

data class Section(
    val name: String, val revisionUnit: String?, val block: Block?
) : Action {
    companion object {
        fun xml(k: Konsumer): Section {
            k.checkCurrent("InsertSection")
            val name: String = k.attributes.getValue("name")

            var revisionUnit: String? = null
            var block: Block? = null

            k.allChildrenAutoIgnore(Names.of("RevisionUnit", "Block")) {
                when (localName) {
                    "RevisionUnit" -> this.child("UnitName") { revisionUnit = this.attributes.getValueOrNull("value") }
                    "Block" -> block = Block.xml(this)
                }
            }

            return Section(name, revisionUnit, block)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        bdtSolver.addActionToSequence(this)
        block?.evaluate(bdtSolver)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        sequence.add(this)
        block?.gather(sequence)
        return sequence
    }
}
