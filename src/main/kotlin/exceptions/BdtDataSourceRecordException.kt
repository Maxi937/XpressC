package exceptions

import models.datasource.DataSource

class BdtDataSourceRecordException(
    private val name: String,
    private val dataSource: DataSource,
    cause: Throwable? = null
) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Datasource Record Access Violation: [${name}] not within DataSource \n\nTABLE RANGES:\n"

        dataSource.recordSets.forEachIndexed { index, recordset ->
            message = message + "[$index]" + "\t" + recordset.name + "\n"
        }
        return message
    }
}