
import api.DartClient
import kotlinx.coroutines.*
import models.CandidateXml.DataSource
import models.Content.ContentItemsDb
import models.bdtXml.Bdt
import models.bdtXml.actions.Action
import models.bdtXml.actions.InsertTextpiece
import org.json.JSONArray
import org.json.JSONObject
import utils.SubdocumentBdtProvider
import utils.deleteResultFile
import utils.writeSequenceToFile
import java.io.File
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.system.exitProcess


fun parseArgs(args: Array<String>) {
    args.forEach { arg ->
        println(arg)
    }
}

fun main(args: Array<String>) {
    if (args.size == 1) {
        val documentName = args[0]
        val bdt = runBlocking { Bdt.fromNetwork(documentName) }
        val response = JSONObject()
        response.put("success", true)
        response.put("bdt", bdt.name)
        response.put("serverVer", bdt.serverVer)
        response.put("bdtsequence", bdt.getSequence())
        response.put("revisionUnits", JSONArray(bdt.getRevisionUnits()))
        println(response)
        exitProcess(0)
    }

//    val contentDb = ContentItemsDb.fromNetwork("GSLOT-11133-XP_Face_Page")

//    val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\ok_stop_loss.xml")
//
//    val (basesequence, sequence) = bdt.solve(dataSource, contentDb, SubdocumentBdtProvider("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract"))
//    println(bdt.sequence)
//    compareOnContentItems(basesequence, sequence)
//    jsonResponse(basesequence, sequence)
//    exitProcess(1)
}

fun fromLocal() {
    val bdt = Bdt.fromFilePath("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract\\GSLOT-11133-XP_Face_Page\\GSLOT-11133-XP_Face_Page\\BDT_Structure.xml")
    val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, "C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\ok_stop_loss.xml")
    val contentDb = ContentItemsDb.fromCsv(File("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract\\GSLOT-11133-XP_Face_Page\\GSLOT-11133-XP_Face_Page\\Content_Items.csv"))
    val (basesequence, sequence) = bdt.solve(dataSource, contentDb, SubdocumentBdtProvider("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract"))
    contentDb.getContentItems()
    compareOnContentItems(basesequence, sequence)
    jsonResponseSuccess(basesequence, sequence)
}


fun fromBatFile(args: Array<String>) {
    val bdt = Bdt.fromFilePath(args[0])
    val dataSource = DataSource.fromFilePath(bdt.primaryDataSource, args[1])
    val contentDb = ContentItemsDb.fromCsv(File("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract\\GSLOT-11133-XP_Face_Page\\GSLOT-11133-XP_Face_Page\\Content_Items.csv"))
    val (basesequence, sequence) = bdt.solve(dataSource, contentDb, SubdocumentBdtProvider("C:\\Users\\YK09\\Development\\Projects\\xpression-test-coverage\\src\\main\\resources\\Test-Data\\Stop_Loss_Contract"))
    jsonResponseSuccess(basesequence, sequence)
}

fun jsonResponseFail() {
    val response = JSONObject()
    response.put("success", false)
    println(response)
}

fun jsonResponseSuccess(basesequence: ArrayList<Action>, sequence: ArrayList<Action>) {
    val baseContentItems = basesequence.filterIsInstance<InsertTextpiece>()
    val pathTakenContentItems = sequence.filterIsInstance<InsertTextpiece>()

    val response = JSONObject()
    response.put("base", JSONArray(baseContentItems))
    response.put("pathTaken", JSONArray(pathTakenContentItems))
    println(response)
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