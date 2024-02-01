package Actions.Subdocument

import java.io.File

/**
 * This global object is a workaround for a Bdt Subdocument Action calling for a Subdocument.
 * The Bdt Solver will call this method with the name of the Subdocument it needs for the sequence.
 * This workaround will not be required when development finished.
 */
class SubdocumentBdtProvider(var directory: String) {
    fun getBdt(name: String): String {
        return File("$directory/$name.xml").readText()
    }
}
