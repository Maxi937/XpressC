package models.syntax.containers

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore
import interfaces.Action
import interfaces.Container
import models.compiler.Compiler
import models.compiler.Instructions
import org.json.JSONObject
import java.util.*

data class Section(
    val name: String,
    val revisionUnits: ArrayList<String>,
    override val instructions: Instructions<Action>,
    override var uuid: UUID = UUID.randomUUID(),
) : Container {

    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun setup(compiler: Compiler) {
        super.setup(compiler)
    }

    override fun toJson(): JSONObject {
        val result = JSONObject()
        result.put("revisionUnits", revisionUnits)
        result.put("name", name)
        result.put("block", instructions)
        return result
    }

    override fun copy(): Action {
        return this.copy(name, revisionUnits, instructions, uuid = uuid)
    }


    companion object {
        fun xml(k: Konsumer): Section {
            k.checkCurrent("InsertSection")
            val name: String = k.attributes.getValue("name")

            val revisionUnits: ArrayList<String> = ArrayList()
            var block = Block(Instructions())

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

            return Section(name, revisionUnits, block.getIntructions())
        }
    }

}
