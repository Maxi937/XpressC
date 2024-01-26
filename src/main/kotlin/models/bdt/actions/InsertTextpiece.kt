package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdt.ObjectRefListVar

data class InsertTextpiece(
    val name: String, val noOfObject: String, val requiredFlag: String, val objectRefListVar: ObjectRefListVar
) : Action {
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

    override fun evaluate() {
        println(this)
    }
}