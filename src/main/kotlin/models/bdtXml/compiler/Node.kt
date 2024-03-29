package models.bdtXml.compiler

data class Node<T>(var value: T, var next: Node<T>? = null) {
    override fun toString(): String {
        if (next != null) {
            return "${value!!::class.java.simpleName} -> ${next!!}"
        } else {
            if (value != null) {
                return value!!::class.java.simpleName.toString()
            }
            return this.javaClass.simpleName
        }
    }
}