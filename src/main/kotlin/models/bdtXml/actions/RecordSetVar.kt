package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class RecordSetVar(
    val name: String,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): RecordSetVar {
            k.checkCurrent("RecordsetVar")
            val name = k.attributes.getValue("name")
            return RecordSetVar(name)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        val record = name.substring(name.indexOf(":") + 1)
        bdtSolver.setActiveRecord(record)
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }
}
