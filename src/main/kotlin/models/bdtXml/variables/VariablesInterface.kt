package models.bdtXml.variables

import models.bdtXml.compiler.Compiler

interface Var {
    val name: String
    var value: String
    var dType: String
    fun bind(compiler: Compiler): Var?
//  fun set(value: String, bdtState: BdtState) : Var

}