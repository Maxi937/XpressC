
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.BDT
import models.bdtXml.actions.Action
import utils.deleteResultFile
import utils.writeSequenceToFile
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat

//"./src/Test-Data/Stop_Loss_Contract/Definition/Definition/BDT_Structure.xml"
object bdtFolder {
    fun getBdt(name: String): String {
        return File("./src/Test-Data/Stop_Loss_Contract/$name/$name/BDT_Structure.xml").readText()
    }
}

fun main(args: Array<String>) {
    val bdts = "GSLOT-11133-XP_Stop_Loss_Contract"

    val bdtString = File("./src/Test-Data/Stop_Loss_Contract/$bdts/$bdts/BDT_Structure.xml").readText()
    val candidateXmlString = File("./src/Test-Data/alaska.xml").readText()

    val bdt = BDT.fromXmlString(bdtString)
    val dataSource = DataSource.fromXmlString(candidateXmlString)
    val contentDb = ContentItemsDb.fromCsv(File("./src/Test-Data/Stop_Loss_Contract/${bdt.name}/${bdt.name}_Content_Items.csv"))

    val (basesequence, sequence) = bdt.solve(dataSource, contentDb)
    debugSequence(basesequence)
    printResult(basesequence, sequence)
}

fun debugSequence(sequence: ArrayList<Action>) {
    sequence.forEach {
        println(it)
    }
}

fun printResult(basesequence: ArrayList<Action>, sequence: ArrayList<Action>) {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    val percentage = (sequence.count().toDouble()/basesequence.count()) * 100
    println("Base: ${basesequence.count()}")
    println("Path Taken: ${sequence.count()}")
    println("Coverage: ${df.format(percentage)}%")

    deleteResultFile("basesequence")
    deleteResultFile("sequence")
    writeSequenceToFile(basesequence, "basesequence")
    writeSequenceToFile(sequence, "sequence")
}