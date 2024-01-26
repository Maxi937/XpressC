package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names
import models.bdt.CrQueryLogicRef
import models.bdt.ObjectRefListVar

data class CrQuery(
    val objectRefListVar: ObjectRefListVar, val crQueryLogicRef: CrQueryLogicRef
) : Action {
    companion object {
        fun xml(k: Konsumer): CrQuery {
            k.checkCurrent("CRQuery")

            var objectRefListVar: ObjectRefListVar? = null
            var crQueryLogicRef: CrQueryLogicRef? = null

            k.children(Names.of("ObjectRefListVar", "CRQueryLogicRef")) {
                when(localName) {
                    "ObjectRefListVar" -> objectRefListVar = ObjectRefListVar.xml(this)
                    "CRQueryLogicRef" -> crQueryLogicRef = CrQueryLogicRef.xml(this)
                }
            }

            return CrQuery(objectRefListVar!!, crQueryLogicRef!!)
        }
    }

    override fun evaluate() {
        println(this)
    }
}