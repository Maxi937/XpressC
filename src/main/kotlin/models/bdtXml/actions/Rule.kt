package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer

data class Rule(
    val name: String
) : Action {
    companion object {
        fun xml(k: Konsumer): Rule {
            k.checkCurrent("CurrentRule")
            val name: String = k.attributes.getValue("name")
            return Rule(name)
        }
    }

    override fun evaluate() {
        println(this)
    }
}
