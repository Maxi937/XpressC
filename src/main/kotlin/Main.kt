import com.gitlab.mvysny.konsumexml.konsumeXml
import models.CandidateXml.Candidate
import models.bdtXml.BDT
import java.io.File
import java.net.URL


fun readFileLineByLineUsingForEachLine(fileName: String) {
    File(fileName).forEachLine { println(it) }
}

fun mapXmlFromFile(xmlFilePath: String): BDT {
    val file = File(xmlFilePath)
    return file.konsumeXml().child("BDT") { BDT.xml(this) }
}

fun mapXmlFromString(xmlString: String): BDT {
    return xmlString.konsumeXml().child("BDT") { BDT.xml(this) }
}

fun main(args: Array<String>) {
    val bdtString = File("./src/TestData/Stop_Loss_Contract/Definition/Definition/BDT_Structure.xml").readText()
    val candidateXmlString = File("./src/TestData/alaska.xml").readText()

    val bdt = mapXmlFromString(bdtString)
    val candidateXml = Candidate.fromString(candidateXmlString)
    bdt.solve(candidateXml)
}