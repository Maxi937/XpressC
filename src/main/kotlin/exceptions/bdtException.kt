package exceptions

import models.CandidateXml.DataSource
import models.CandidateXml.RecordSet
import models.CandidateXml.Table
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

class BdtDataSourceRecordException(private val name: String, private val dataSource: DataSource, cause: Throwable? = null) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Datasource Record Access Violation: [${name}] not within DataSource\n\nTABLE RANGES:\n"

        dataSource.recordSets.forEachIndexed { index, recordset ->
            message = message + "[$index]" + "\t" + recordset.name + "\n"
        }
        return message
    }
}
class BdtDataSourceFieldAccessException(private val accessedVariable: String, private val recordSet: Table, private val currentRule: String, cause: Throwable? = null) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Datasource Field Access: $accessedVariable at Rule $currentRule\n\nRECORDSET STATE:\n"

        recordSet.columns.forEach {
            message = message + it + "\n"
        }
        return message
    }
}

// Exception where the ContentDb cannot be read
class ContentException(override val message: String?) : Exception(message) {
}