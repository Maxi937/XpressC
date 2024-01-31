package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.BdtSolver

data class Section(
    val name: String, val revisionUnits: ArrayList<String>, val block: Block?
) : Action {
    companion object {
        fun xml(k: Konsumer): Section {
            k.checkCurrent("InsertSection")
            val name: String = k.attributes.getValue("name")

            val revisionUnits: ArrayList<String> = ArrayList()
            var block: Block? = null

            k.allChildrenAutoIgnore(Names.of("RevisionUnit", "Block")) {
                when (localName) {
                    "RevisionUnit" -> this.children("UnitName") { revisionUnits.add(this.attributes.getValueOrNull("value").toString()) }
                    "Block" -> block = Block.xml(this)
                }
            }

            return Section(name, revisionUnits, block)
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
