package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import interfaces.Action
import models.compiler.Compiler
import org.json.JSONObject
import java.util.*


data class Jump(
    val toLabel: String,
    override var uuid: UUID = UUID.randomUUID()
) : Action {

    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(toLabel, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): Jump {
            k.checkCurrent("Jump")
            val toLabel = k.attributes.getValue("toLabel")
            return Jump(toLabel)
        }
    }

}