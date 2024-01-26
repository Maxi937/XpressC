package models.bdt.actions

import com.gitlab.mvysny.konsumexml.Konsumer
import com.gitlab.mvysny.konsumexml.Names

data class Block(
    val actions: ArrayList<Action>,
) : Action {
    companion object {
        fun xml(k: Konsumer): Block {
            k.checkCurrent("Block")

            val actions: ArrayList<Action> = ArrayList()

            k.children(Names.any()) {
                val action = whichAction(this)

                if (action != null) {
                    actions.add(action)
                }

            }

            return Block(actions)
        }
    }

    override fun evaluate() {
        println(this)
    }
}
