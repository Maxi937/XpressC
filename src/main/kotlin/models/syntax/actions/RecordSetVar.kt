package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import interfaces.Action
import models.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class RecordSetVar(
    val name: String,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    override fun evaluate(compiler: Compiler): Boolean {
        val record = name.substring(name.indexOf(":") + 1)
        compiler.setActiveRecord(record)
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(name, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): RecordSetVar {
            k.checkCurrent("RecordsetVar")
            val name = k.attributes.getValue("name")
            return RecordSetVar(name)
        }
    }
}
