package exceptions

import interfaces.Var

// Exceptions where the BDT state is invalid, like double revision units or normal crash occurances in an xpression document
class BdtVariableAccessException(
    private val accessedVariable: String,
    val variables: ArrayList<Var>,
    cause: Throwable? = null
) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Variable Access: $accessedVariable\nBDT STATE:\n"

        variables.forEach {
            message = message + it + "\n"
        }
        return message
    }
}