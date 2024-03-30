package api

import api.models.BdtXmlApiResponse
import api.models.ContentGroupApiResponse
import api.models.NetworkResult
import api.models.XpressionDocumentModelApiResponse
import interfaces.ApiHandlerInterface
import interfaces.ApiServices
import interfaces.DartServiceInterface

class DartService(private val apiServices: ApiServices) : DartServiceInterface, ApiHandlerInterface {
    override suspend fun getDocumentModels(env: String): NetworkResult<XpressionDocumentModelApiResponse> =
        handleApi { apiServices.getDocumentModels(env) }

    override suspend fun getBdtXml(documentName: String, env: String): NetworkResult<BdtXmlApiResponse> =
        handleApi { apiServices.getBdtXml(documentName, env) }

    override suspend fun getBdtXml(documentId: Long, env: String): NetworkResult<BdtXmlApiResponse> =
        handleApi { apiServices.getBdtXml(documentId, env) }

    override suspend fun getContentGroup(
        documentName: String,
        textClassId: Int,
        env: String
    ): NetworkResult<ContentGroupApiResponse> =
        handleApi { apiServices.getContentGroup(documentName, textClassId, env) }
}