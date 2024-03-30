package interfaces

import interfaces.Action
import models.compiler.Instructions


interface Container : Action {
    val instructions: Instructions<Action>
}
