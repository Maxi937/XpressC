package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdtXml.CrQueryLogicRef
import models.bdtXml.ObjectRefListVar
import models.bdtXml.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class CrQuery(
    val objectRefListVar: ObjectRefListVar,
    val crQueryLogicRef: CrQueryLogicRef,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    override fun evaluate(compiler: Compiler): Boolean {
        compiler.crRequests.add(compiler.getVariable("TEXTCLASS_ID")?.value!!.toLong())
        compiler.crLength += 1
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(
            objectRefListVar = this.objectRefListVar,
            crQueryLogicRef = this.crQueryLogicRef,
            uuid = this.uuid,

            )
    }

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
}