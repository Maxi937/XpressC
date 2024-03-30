package models.syntax.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import interfaces.Action
import models.syntax.misc.ObjectRefListVar
import models.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class Reset(
    val objectRefListVar: ObjectRefListVar,
    override var uuid: UUID = UUID.randomUUID()
) : Action {

    override fun evaluate(compiler: Compiler): Boolean {
        if (objectRefListVar.name == "DLSTP") {
            compiler.crLength = 0
        }

        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(objectRefListVar, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): Reset {
            k.checkCurrent("Reset")

            var objectRefListVar: ObjectRefListVar? = null

            k.child("ObjectRefListVar") {
                objectRefListVar = ObjectRefListVar.xml(this)
            }

            return Reset(objectRefListVar!!)
        }
    }
}