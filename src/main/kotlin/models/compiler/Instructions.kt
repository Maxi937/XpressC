package models.compiler

import org.json.JSONArray
import org.json.JSONObject

class Instructions<T> : Iterable<T>, Collection<T>, MutableIterable<T>, MutableCollection<T> {
    private var head: Node<T>? = null
    private var tail: Node<T>? = null
    override var size = 0
        private set

    override fun clear() {
        head = null
        tail = null
        size = 0
    }

    override fun addAll(elements: Collection<T>): Boolean {
        for (element in elements) {
            append(element)
        }
        return true
    }

    override fun add(element: T): Boolean {
        append(element)
        return true
    }

    override fun containsAll(elements: Collection<T>): Boolean {
        for (searched in elements) {
            if (!contains(searched)) return false
        }
        return true
    }

    override fun contains(element: T): Boolean {
        for (item in this) {
            if (item == element) return true
        }
        return false
    }

    fun push(value: T): Instructions<T> {
        head = Node(value = value, next = head)

        if (tail == null) {
            tail = head
        }
        size++
        return this
    }

    fun removeAfter(node: Node<T>): T? {
        val result = node.next?.value

        if (node.next == tail) {
            tail = node
        }

        if (node.next != null) {
            size--
        }

        node.next = node.next?.next
        return result
    }

    fun removeLast(): T? {
        // 1
        val head = head ?: return null
        // 2
        if (head.next == null) return pop()
        // 3
        size--

        // 4
        var prev = head
        var current = head

        var next = current.next
        while (next != null) {
            prev = current
            current = next
            next = current.next
        }
        // 5
        prev.next = null
        tail = prev
        return current.value
    }

    fun pop(): T? {
        if (!isEmpty()) size--
        val result = head?.value
        head = head?.next
        if (isEmpty()) {
            tail = null
        }

        return result
    }

    fun insert(value: T, afterNode: Node<T>): Node<T> {
        // 1
        if (tail == afterNode) {
            append(value)
            return tail!!
        }
        // 2
        val newNode = Node(value = value, next = afterNode.next)
        // 3
        afterNode.next = newNode
        size++
        return newNode
    }


    fun nodeAt(index: Int): Node<T>? {
        // 1
        var currentNode = head
        var currentIndex = 0

        // 2
        while (currentNode != null && currentIndex < index) {
            currentNode = currentNode.next
            currentIndex++
        }

        return currentNode
    }

    fun append(value: T) {
        // 1
        if (isEmpty()) {
            push(value)
            return
        }


        // 2
        tail?.next = Node(value = value)

        // 3
        tail = tail?.next
        size++
    }

    override fun isEmpty(): Boolean {
        return size == 0
    }


    override fun iterator(): MutableIterator<T> {
        return InstructionsIterator(this)
    }

    override fun retainAll(elements: Collection<T>): Boolean {
        var result = false
        val iterator = this.iterator()
        while (iterator.hasNext()) {
            val item = iterator.next()
            if (!elements.contains(item)) {
                iterator.remove()
                result = true
            }
        }
        return result
    }

    override fun removeAll(elements: Collection<T>): Boolean {
        var result = false
        for (item in elements) {
            result = remove(item) || result
        }
        return result
    }

    override fun remove(element: T): Boolean {
        // 1
        val iterator = iterator()
        // 2
        while (iterator.hasNext()) {
            val item = iterator.next()
            // 3
            if (item == element) {
                iterator.remove()
                return true
            }
        }
        // 4
        return false
    }

    override fun toString(): String {
        return if (isEmpty()) {
            "Empty list"
        } else {
            "Instruction<${head.toString()}>"
        }
    }


}