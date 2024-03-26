package api

import api.models.BdtXmlApiResponse
import api.models.ContentGroupApiResponse
import api.models.NetworkResult
import api.models.XpressionDocumentModelApiResponse


interface DartService {
    suspend fun getDocumentModels(env: String): NetworkResult<XpressionDocumentModelApiResponse>

    suspend fun getBdtXml(documentName: String, env: String): NetworkResult<BdtXmlApiResponse>
    suspend fun getBdtXml(documentId: Long, env: String): NetworkResult<BdtXmlApiResponse>

    suspend fun getContentGroup(
        documentName: String,
        textClassId: Int,
        env: String
    ): NetworkResult<ContentGroupApiResponse>

}

class DartServiceImpl(private val apiServices: ApiServices) : DartService, ApiHandler {
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