package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class Label(
    val name: String,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    companion object {
        fun xml(k: Konsumer): Label {
            k.checkCurrent("Label")
            val name = k.attributes.getValue("name")
            return Label(name)
        }
    }

    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(name, uuid = uuid)
    }

}