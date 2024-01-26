package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import com.gitlab.mvysny.konsumexml.allChildrenAutoIgnore

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

    override fun evaluate() {
        println(this)
    }
}
