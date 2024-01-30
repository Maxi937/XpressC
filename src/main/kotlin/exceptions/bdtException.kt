package exceptions

import models.bdtXml.variables.Var


// mainly xml parsing exceptions upon Konsume
class BdtParsingException(override val message: String?) : Exception(message) {
}

// Exceptions where the BDT state is invalid, like double revision units or normal crash occurances in an xpression document
class BdtException(override val message: String?) : Exception(message) {
}

// Exceptions where the BDT state is invalid, like double revision units or normal crash occurances in an xpression document
class BdtVariableAccessException(private val accessedVariable: String, val variables: ArrayList<Var>, cause: Throwable? = null) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Variable Access: $accessedVariable\nBDT STATE:\n"

        variables.forEach {
            message = message + it + "\n"
        }
        return message
    }
}


// Exceptions where the Candidate file cannot be read and sourced into a Datasource
class CandidateException(override val message: String?) : Exception(message) {
}


// Exception where the ContentDb cannot be read
class ContentException(override val message: String?) : Exception(message) {
}