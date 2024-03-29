package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler
import org.json.JSONObject
import java.util.*


data class RecordSetMoveNext(
    val recordSetVar: RecordSetVar,
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    companion object {
        fun xml(k: Konsumer): RecordSetMoveNext {
            k.checkCurrent("RecordsetMoveNext")

            var recordSetVar: RecordSetVar? = null

            k.child("RecordsetVar") {
                recordSetVar = RecordSetVar.xml(this)

            }

            return RecordSetMoveNext(recordSetVar!!)
        }
    }

    override fun evaluate(compiler: Compiler): Boolean {
        compiler.recordSetMoveNext()
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(recordSetVar, uuid = uuid)
    }

}