import com.gitlab.mvysny.konsumexml.konsumeXml
import models.bdt.BDT
import models.CandidateXml.CandidateXml
import java.io.File


fun readFileLineByLineUsingForEachLine(fileName: String) {
    File(fileName).forEachLine { println(it) }
}

fun mapXml(xmlFilePath: String): BDT {
    val file = File(xmlFilePath)
    return file.konsumeXml().child("BDT") { BDT.xml(this) }
}


fun main(args: Array<String>) {
    val bdt = mapXml("./TestData/Stop_Loss_Contract/Definition/Definition/BDT_Structure.xml")
    val candidateXml = CandidateXml.fromFile(File("./TestData/alaska.xml"))
    bdt.solve(candidateXml)
}