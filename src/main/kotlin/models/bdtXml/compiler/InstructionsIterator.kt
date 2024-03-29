package models.bdtXml.compiler

class InstructionsIterator<T>(val instructions: Instructions<T>) : MutableIterator<T> {
    private var index = 0
    private var lastNode: Node<T>? = null
    override fun next(): T {
        if (index >= instructions.size) throw IndexOutOfBoundsException()
        lastNode = if (index == 0) {
            instructions.nodeAt(0)
        } else {
            lastNode?.next
        }

        index++
        return lastNode!!.value
    }

    override fun remove() {
        TODO("Not yet implemented")
    }

    override fun hasNext(): Boolean {
        return index < instructions.size
    }
}