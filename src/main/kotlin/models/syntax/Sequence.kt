package models.syntax

import interfaces.Action
import org.json.JSONArray
import java.util.*

class Sequence(val actions: ArrayList<Action> = ArrayList()) {

    fun toJson(): JSONArray {
        val arr = JSONArray()

        execute { action ->
            arr.put(action)
        }

        return arr
    }

    fun execute(function: (action: Action) -> Unit) {
        actions.forEach { it ->
            function(it)
        }
    }

    fun execute(function: (index: Int, action: Action) -> Unit) {
        actions.forEachIndexed { index, action ->
            function(index, action)
        }
    }

    fun add(action: Action) {
        actions.add(action)
    }

    fun add(sequence: Sequence) {
        this.actions += sequence.actions
    }

    fun add(actions: ArrayList<Action>) {
        this.actions += actions
    }

    fun size(): Int {
        return actions.size
    }
    
}