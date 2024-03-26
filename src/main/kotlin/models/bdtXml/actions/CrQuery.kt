package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.CrQueryLogicRef
import models.bdtXml.ObjectRefListVar
import models.bdtXml.bdtsolver.BdtSolver
import org.json.JSONObject

data class CrQuery(
    val objectRefListVar: ObjectRefListVar, val crQueryLogicRef: CrQueryLogicRef,
    override var sequenceId: Int = 0,
    var evaluated: Boolean = false
) : Action {
    companion object {
        fun xml(k: Konsumer): CrQuery {
            k.checkCurrent("CRQuery")

            var objectRefListVar: ObjectRefListVar? = null
            var crQueryLogicRef: CrQueryLogicRef? = null

            k.children(Names.of("ObjectRefListVar", "CRQueryLogicRef")) {
                when (localName) {
                    "ObjectRefListVar" -> objectRefListVar = ObjectRefListVar.xml(this)
                    "CRQueryLogicRef" -> crQueryLogicRef = CrQueryLogicRef.xml(this)
                }
            }

            return CrQuery(objectRefListVar!!, crQueryLogicRef!!)
        }
    }

    override fun evaluate(bdtSolver: BdtSolver) {
        evaluated = true
        bdtSolver.crRequests.add(bdtSolver.getVariable("TEXTCLASS_ID")?.value!!.toLong())
        bdtSolver.crLength += 1
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

}