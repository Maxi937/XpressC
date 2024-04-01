import kotlinx.coroutines.runBlocking
import models.Bdt
import models.assetprovider.NetworkAssetProvider
import models.datasource.DataSource
import org.json.JSONArray
import org.json.JSONObject
import java.nio.file.Path
import kotlin.io.path.Path
import kotlin.system.exitProcess

fun main(args: Array<String>) {
    val job = args[0]

    if (job == "compile") {
        val jobPath = args[1]
        val canPath = args[2]
        val env = args[3]
        compile(Path(jobPath), Path(canPath), env)
    }

//    if (job == "analyse") {
//        val jobPath = args[1]
//        val env = args[2]
//        analyse(Path(jobPath), env)
//    }

    if (job == "bdt") {
        val jobPath = args[1]
        val env = args[2]
        runBlocking {
            getBdt(jobPath.toLong(), env)
        }
    }
}

suspend fun getBdt(documentId: Long, env: String) {
    val bdt = Bdt.fromNetwork(documentId, env)
    println(bdt.sequence)
}

//fun analyse(jobPath: Path, env: String) {
//    val bdt = Bdt.fromFilePath(jobPath.resolve("bdt.xml").toString())
//    val response = JSONObject()
//    response.put("success", true)
//    response.put("bdt", bdt.name)
//    response.put("serverVer", bdt.serverVer)
//    response.put("bdtsequence", bdt.toJson())
//    response.put("revisionUnits", JSONArray(bdt.getRevisionUnits()))
//    println(response)
//    exitProcess(0)
//}

fun compile(jobPath: Path, canPath: Path, env: String) {
    val assetProvider = NetworkAssetProvider(env)
    val bdt = Bdt.fromFilePath(jobPath.toString())
    val dataSource = DataSource.fromFilePath(canPath.toString())
    val result = bdt.compile(dataSource, assetProvider)
    println(result.toJson())
    exitProcess(0)
}

fun jsonResponseFail(errMessage: String?) {
    val response = JSONObject()
    response.put("success", false)
    val error = errMessage ?: "unknown"
    response.put("error", error)
    System.err.println(response)
}