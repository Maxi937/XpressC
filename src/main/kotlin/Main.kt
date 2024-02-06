
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import models.bdtXml.actions.Action
import models.bdtXml.actions.InsertTextpiece
import utils.SubdocumentBdtProvider
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
    val bdts = "GSLOT-11133-XP_Schedule_of_Benefit_Aggregate_ADF_Table"
    val bdtString = File("./src/main/resources/Test-Data/Stop_Loss_Contract/$bdts/$bdts/BDT_Structure.xml").readText()
    val candidateXmlString = File("src/test/resources/Candidate/Stop_Loss_2023SL_AK.xml").readText()

    val bdt = Bdt.fromXmlString(bdtString)

    val dataSource = DataSource.fromXmlString(bdt.primaryDataSource, candidateXmlString)
    val contentDb = ContentItemsDb.fromCsv(File("./src/main/resources/Test-Data/Stop_Loss_Contract/${bdt.name}/${bdt.name}_Content_Items.csv"))
    val (basesequence, sequence) = bdt.solve(dataSource, contentDb, SubdocumentBdtProvider("./src/main/resources/Test-Data/Stop_Loss_Contract/"))

    compareOnContentItems(basesequence, sequence)
//    compareOnTotalSequence(basesequence, sequence)

}

fun debugDataSource(dataSource: DataSource) {
    println(dataSource.name)
    dataSource.tables.forEach {
        println(it)
    }
}

fun debugSequence(sequence: ArrayList<Action>) {
    sequence.forEach {
        println(it)
    }
}

fun compareOnContentItems(basesequence: ArrayList<Action>, sequence: ArrayList<Action>) {
    val baseContentItems = basesequence.filterIsInstance<InsertTextpiece>()
    val pathTakenContentItems = sequence.filterIsInstance<InsertTextpiece>()

    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    val percentage = (pathTakenContentItems.count().toDouble() / baseContentItems.count()) * 100
    println("Base: ${baseContentItems.count()}")
    println("Path Taken: ${pathTakenContentItems.count()}")
    println("Coverage: ${df.format(percentage)}%")
    writeResult(ArrayList(baseContentItems), ArrayList(pathTakenContentItems))
}

fun compareOnTotalSequence(basesequence: ArrayList<Action>, sequence: ArrayList<Action>) {
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING
    val percentage = (sequence.count().toDouble() / basesequence.count()) * 100
    println("Base: ${basesequence.count()}")
    println("Path Taken: ${sequence.count()}")
    println("Coverage: ${df.format(percentage)}%")
    writeResult(basesequence, sequence)

}

fun writeResult(basesequence: ArrayList<Action>, sequence: ArrayList<Action>) {
    println("\n")
    deleteResultFile("basesequence")
    deleteResultFile("sequence")
    writeSequenceToFile(basesequence, "basesequence")
    writeSequenceToFile(sequence, "sequence")
}