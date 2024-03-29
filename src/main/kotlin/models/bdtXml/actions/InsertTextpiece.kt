package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.ObjectRefListVar
import models.bdtXml.compiler.Compiler
import org.json.JSONObject
import java.util.*

data class InsertTextpiece(
    val name: String,
    val noOfObject: String,
    val requiredFlag: String,
    val objectRefListVar: ObjectRefListVar,
    var textClassId: Long = 0,
    override var uuid: UUID = UUID.randomUUID()
) : Action {


    override fun evaluate(compiler: Compiler): Boolean {
        val id = compiler.getVariable("TEXTCLASS_ID")

        if (id != null) {
            textClassId = id.value.toLong()

            if (textClassId >= 1) {
                return true
            }
        }
        return false
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(name, noOfObject, requiredFlag, objectRefListVar, textClassId, uuid = uuid)
    }

    companion object {
        fun xml(k: Konsumer): InsertTextpiece {
            k.checkCurrent("InsertTextpiece")

            val name: String = k.attributes.getValue("name")
            val noOfObject: String = k.attributes.getValue("noOfObject")
            val requiredFlag: String = k.attributes.getValue("requiredFlag")
            var objectRefListVar: ObjectRefListVar? = null

            k.child("ObjectRefListVar") {
                objectRefListVar = ObjectRefListVar.xml(this)
            }

            return InsertTextpiece(name, noOfObject, requiredFlag, objectRefListVar!!)
        }
    }


}