package utils

import models.bdtXml.actions.Action
import models.bdtXml.actions.If
import models.bdtXml.actions.Section
import java.io.File


fun writeSequenceToFile(sequence: ArrayList<Action>, fileName: String) {
    sequence.forEach {
        File("./src/Test-Data/$fileName.txt").appendText("$it\n")
    }

}
fun stringToDate(dateStr: String) {

}
fun unwrapAction(action: Action) {
    when (action) {
        is If -> unwrapIf(action)
        else -> println("-\t$action")
    }
}

fun unwrapIf(action: If) {
    if (action.block.actions.isEmpty()) {
        println("-\tSkip")
    } else {
        action.block.actions.forEach {
            println("-\t$it")
        }

    }

}