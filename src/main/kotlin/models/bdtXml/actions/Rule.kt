package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class Rule(
    val name: String,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(name, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): Rule {
            k.checkCurrent("CurrentRule")
            val name: String = k.attributes.getValue("name")
            return Rule(name)
        }
    }
}
