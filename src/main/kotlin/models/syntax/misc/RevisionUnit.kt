package models.syntax.misc

import com.gitlab.mvysny.konsumexml.Konsumer
import models.compiler.Compiler
import models.datasource.Query


interface RevisionUnit {
    var value: String
    fun evaluate(compiler: Compiler)

}


data class RevisionUnitDb(private val dsGrp:String, val field: String, val table: String, override var value:String = "") : RevisionUnit {
    override fun evaluate(compiler: Compiler) {
        val field = compiler.getDbVariable(field)

        if(field != null) {
            value = field
        }
    }
}

data class RevisionUnitValue(override var value:String) : RevisionUnit {

    override fun evaluate(compiler: Compiler) {

    }
}

fun whichRevisionType(konsumer: Konsumer) : RevisionUnit? {
    val value = konsumer.attributes.getValueOrNull("value")

    if(value != null) {
        return RevisionUnitValue(value)
    }

    val dsGrp = konsumer.attributes.getValueOrNull("dsGrp")
    val field = konsumer.attributes.getValueOrNull("field")
    val table = konsumer.attributes.getValueOrNull("table")

    if(dsGrp != null && field !=null && table != null) {
        return RevisionUnitDb(dsGrp, field, table)
    }

    return null

}