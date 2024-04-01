package models.syntax.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import interfaces.Container
import models.compiler.Compiler
import models.compiler.Instructions
import models.syntax.misc.RevisionUnit
import models.syntax.misc.whichRevisionType
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

data class Section(
    val name: String,
    private val revisionUnits: ArrayList<RevisionUnit>,
    override val instructions: Instructions<Action>,
    override var uuid: UUID = UUID.randomUUID(),
) : Container {

    fun getRevisionUnit() : String? {
        if(revisionUnits.isEmpty()) {
            return null
        }
        var revName = ""

        revisionUnits.forEach {
            revName = revName.plus(it.value)
        }

        return revName
    }

    override fun evaluate(compiler: Compiler): Boolean {
        revisionUnits.forEach {
            it.evaluate(compiler)
        }
        return true
    }

    override fun setup(compiler: Compiler) {
        super.setup(compiler)
    }

    override fun toJson(): JSONObject {
        val result = JSONObject()
        result.put("revisionUnit", getRevisionUnit())
        result.put("name", name)

        val block = JSONArray()

         instructions.forEach {
            val obj = JSONObject()
            obj.put(it.javaClass.simpleName, it.toJson())
            block.put(obj)
        }

        result.put("block", block)
        return result
    }

    override fun copy(): Action {
        return this.copy(name, revisionUnits, instructions, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): Section {
            k.checkCurrent("InsertSection")
            val name: String = k.attributes.getValue("name")

            val revisionUnits: ArrayList<RevisionUnit> = ArrayList()
            var block = Block(Instructions())

            k.allChildrenAutoIgnore(Names.of("RevisionUnit", "Block")) {
                when (localName) {
                    "RevisionUnit" -> this.children("UnitName") {
                        val revisionUnit = whichRevisionType(this)

                        if(revisionUnit != null) {
                            revisionUnits.add(revisionUnit)
                        }
                    }
                    "Block" -> block = Block.xml(this)
                }
            }
            return Section(name, revisionUnits, block.getIntructions())
        }
    }

}
