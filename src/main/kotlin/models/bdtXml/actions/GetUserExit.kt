package models.bdtXml.actions


import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler
import models.bdtXml.variables.UserExit
import org.json.JSONObject
import java.util.*


data class GetUserExit(
    val userExit: UserExit,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    companion object {
        fun xml(k: Konsumer): GetUserExit {
            k.checkCurrent("GetUserExitValue")

            val userExit: UserExit = k.child("Variable") {
                return@child UserExit.xml(this)
            }

            return GetUserExit(userExit)
        }
    }

    override fun evaluate(compiler: Compiler): Boolean {
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(userExit, uuid)
    }

}