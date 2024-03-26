package models.bdtXml.actions


import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import models.bdtXml.variables.UserExit
import org.json.JSONObject


data class GetUserExit(
    val userExit: UserExit,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
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

    override fun evaluate(bdtSolver: BdtSolver) {

    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}