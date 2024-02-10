package utils

import java.io.File

/**
 * This global object is a workaround for a Bdt Subdocument Action calling for a Subdocument.
 * The Bdt Solver will call this method with the name of the Subdocument it needs for the sequence.
 * This workaround will not be required when development finished.
 * $directory/$name/$name/BDT_Structure.xml
 */
class SubdocumentBdtProvider(var directory: String) {
    fun getBdt(name: String): String {
        return File("$directory/$name/$name/BDT_Structure.xml").readText()
    }
}
