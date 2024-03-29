package models.bdtXml.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import models.bdtXml.compiler.Compiler
import models.bdtXml.variables.Variable
import org.json.JSONObject
import java.util.*


data class Define(
    val inputParameters: List<Variable>, val variables: List<Variable> = ArrayList(),
    override var uuid: UUID = UUID.randomUUID()
) : Action {
    companion object {
        fun xml(k: Konsumer): Define {
            k.checkCurrent("Define")
            val inputParams = k.child("InputParameters") { children("Variable") { Variable.xml(this) } }
            val variables = k.children("Variable") { Variable.xml(this) }
            return Define(inputParams, variables)
        }
    }

    override fun evaluate(compiler: Compiler): Boolean {
        variables.forEach {
            compiler.bindVariable(it)
        }

        inputParameters.forEach {
            compiler.bindInputParam(it)
        }
        return true
    }

    override fun toJson(): JSONObject {
        return JSONObject(this)
    }

    override fun copy(): Action {
        return this.copy(inputParameters, variables, uuid)
    }
}
