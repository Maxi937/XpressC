package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.BdtSolver
import org.json.JSONObject

data class RecordSetVar(
    val name: String,
) : Action {
    companion object {
        fun xml(k: Konsumer): RecordSetVar {
            k.checkCurrent("RecordsetVar")
            val name = k.attributes.getValue("name")
            return RecordSetVar(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        val record = name.substring(name.indexOf(":") + 1)
        bdtSolver.setActiveRecord(record)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun gather(sequence: ArrayList<Action>): ArrayList<Action> {
        TODO("Not yet implemented")
    }
}
