package models.assetprovider

import api.DartClient
import api.models.NetworkResult
import interfaces.AssetProviderInterface
import kotlinx.coroutines.runBlocking
import models.content.ContentGroup
import models.Bdt

class NetworkAssetProvider(private val env: String) : AssetProviderInterface {

    override fun getBdt(documentId: Long): Bdt {
        return runBlocking { Bdt.fromNetwork(documentId, env) }
    }

    override fun getBdt(documentName: String): Bdt {
        return runBlocking { Bdt.fromNetwork(documentName, env) }
    }

    override fun getContentGroup(documentName: String, textClassId: Int): ContentGroup {
        return runBlocking {
            when (val response = DartClient.service.getContentGroup(documentName, textClassId, env)) {
                is NetworkResult.Success -> return@runBlocking response.data.content
                is NetworkResult.Error -> throw Exception(response.errorMsg)
                is NetworkResult.Exception -> throw Exception(response.e)
            }
        }
    }
}