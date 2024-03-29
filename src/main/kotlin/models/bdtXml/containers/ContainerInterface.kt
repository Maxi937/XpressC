package models.bdtXml.containers

import models.bdtXml.actions.Action
import models.bdtXml.compiler.Instructions


interface Container : Action {
    val instructions: Instructions<Action>
}
