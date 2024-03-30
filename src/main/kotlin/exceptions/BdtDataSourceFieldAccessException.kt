package exceptions

import models.datasource.Table

class BdtDataSourceFieldAccessException(
    private val accessedVariable: String,
    private val recordSet: Table,
    private val currentRule: String,
    cause: Throwable? = null
) :
    Exception(null, cause) {

    override fun toString(): String {
        var message = ": Datasource Field Access: $accessedVariable at Rule $currentRule\n\nRECORDSET STATE:\n"

        recordSet.columns.forEach {
            message = message + it + "\n"
        }
        return message
    }
}