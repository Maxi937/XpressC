package interfaces

import models.compiler.Compiler

interface Var {
    val name: String
    var value: String
    var dType: String
    fun bind(compiler: Compiler): Var?
}