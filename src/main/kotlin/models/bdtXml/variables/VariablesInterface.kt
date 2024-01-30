package models.bdtXml.variables

import models.bdtXml.BdtSolver

interface Var {
  val name: String
  var value: String
  var dType: String

  fun bind(bdtSolver: BdtSolver) : Var?
//  fun set(value: String, bdtState: BdtState) : Var

}