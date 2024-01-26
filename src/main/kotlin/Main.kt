import com.gitlab.mvysny.konsumexml.konsumeXml
import models.CandidateXml.Candidate
import models.bdtXml.BDT
import java.io.File
import java.net.URL

fun main(args: Array<String>) {
    val bdtString = File("./src/Test-Data/Stop_Loss_Contract/Definition/Definition/BDT_Structure.xml").readText()
    val candidateXmlString = File("./src/Test-Data/alaska.xml").readText()

    val bdt = BDT.fromXmlString(bdtString)
    val candidateXml = Candidate.fromXmlString(candidateXmlString)
    bdt.solve(candidateXml)
}