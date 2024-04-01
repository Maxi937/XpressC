package interfaces


import models.compiler.Compiler
import org.json.JSONObject
import java.util.*


interface UnitTestInterface {

    val uuid: UUID

    var pass : Boolean

    fun evaluate(compiler: Compiler)

    fun toJson(): JSONObject

    fun setup(compiler: Compiler) {}

    fun finish(compiler: Compiler) {}

}