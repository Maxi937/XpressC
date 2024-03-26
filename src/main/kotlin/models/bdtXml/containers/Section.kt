package models.bdtXml.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import models.bdtXml.actions.Action
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class Section(
    val name: String, val revisionUnits: ArrayList<String>, val block: Block?,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): Section {
            k.checkCurrent("InsertSection")
            val name: String = k.attributes.getValue("name")

            val revisionUnits: ArrayList<String> = ArrayList()
            var block: Block? = null

            k.allChildrenAutoIgnore(Names.of("RevisionUnit", "Block")) {
                when (localName) {
                    "RevisionUnit" -> this.children("UnitName") {
                        revisionUnits.add(
                            this.attributes.getValueOrNull("value").toString()
                        )
                    }

                    "Block" -> block = Block.xml(this)
                }
            }

            return Section(name, revisionUnits, block)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        block?.evaluate(bdtSolver)
        bdtSolver.addActionToSequence(this)
    }

    override fun setup(bdtSolver: BdtSolver) {
        super.setup(bdtSolver)
        block?.setup(bdtSolver)
    }

    override fun toJson(): JSONObject {
        val result = JSONObject()
        result.put("revisionUnits", revisionUnits)
        result.put("name", name)
        result.put("block", block?.toJsonArray())
        result.put("sequenceId", sequenceId)
        result.put("evaluated", evaluated)
        return result
    }
}
