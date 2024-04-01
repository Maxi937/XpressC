package models.unittest

import interfaces.UnitTestInterface
import models.compiler.Compiler
import models.compiler.CompilerResult
import models.syntax.containers.Section
import org.json.JSONObject
import java.util.*
import kotlin.collections.ArrayList

class RevisionUnitTest() : UnitTestInterface {
    val result = ArrayList<Pair<String, Int>>()
    override val uuid: UUID = UUID.randomUUID()
    override var pass: Boolean = true

    private fun getRevisionUnits(compiler: Compiler): List<String> {
        val units = ArrayList<String>()

        compiler.eventSequence.forEach {
            if(it is Section) {
                val unit = it.getRevisionUnit()
                if(unit != null) {
                    units.add(unit)
                }
            }

        }
        return units
    }
    override fun evaluate(compiler: Compiler) {
        val revUnits = getRevisionUnits(compiler)

        revUnits.forEach { revUnitName ->
            val count = revUnits.count { it == revUnitName }
            result.add(Pair(revUnitName, count))
        }

        result.forEach {
            if(it.second > 1) {
               pass = false
                return
            }
        }
    }

    override fun toJson(): JSONObject {
        val obj = JSONObject()
        obj.put("type", javaClass.simpleName)
        obj.put("pass", pass)
        obj.put("result", result.toSet())

        return obj
    }
}