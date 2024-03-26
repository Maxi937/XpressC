package models.bdtassetprovider

import api.DartClient
import api.models.NetworkResult
import kotlinx.coroutines.runBlocking
import models.Content.ContentGroup
import models.bdtXml.Bdt

class BdtAssetProvider(private val env: String) {

    fun getBdt(documentId: Long): Bdt {
        return runBlocking { Bdt.fromNetwork(documentId, env) }
    }

    fun getBdt(documentName: String): Bdt {
        return runBlocking { Bdt.fromNetwork(documentName, env) }
    }

    fun getContentGroup(documentName: String, textClassId: Int): ContentGroup {
        return runBlocking {
            when (val response = DartClient.service.getContentGroup(documentName, textClassId, env)) {
                is NetworkResult.Success -> return@runBlocking response.data.content
                is NetworkResult.Error -> throw Exception(response.errorMsg)
                is NetworkResult.Exception -> throw Exception(response.e)
            }
        }
    }
}