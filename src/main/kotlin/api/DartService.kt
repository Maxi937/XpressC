package api

import api.models.BdtXmlApiResponse
import api.models.ContentItemsApiResponse
import api.models.NetworkResult
import api.models.XpressionDocumentModelApiResponse
import models.Content.ContentItemsDb


interface DartService {
    suspend fun getDocumentModels(env: String): NetworkResult<XpressionDocumentModelApiResponse>

    suspend fun getBdtXml(documentName: String): NetworkResult<BdtXmlApiResponse>
    suspend fun getBdtXml(documentId: Long): NetworkResult<BdtXmlApiResponse>

    suspend fun getContentItems(documentName: String): NetworkResult<ContentItemsApiResponse>

}

class DartServiceImpl(private val apiServices: ApiServices) : DartService, ApiHandler {
    override suspend fun getDocumentModels(env: String): NetworkResult<XpressionDocumentModelApiResponse> = handleApi { apiServices.getDocumentModels(env) }

    override suspend fun getBdtXml(documentName: String): NetworkResult<BdtXmlApiResponse> = handleApi { apiServices.getBdtXml(documentName) }

    override suspend fun getBdtXml(documentId: Long): NetworkResult<BdtXmlApiResponse> = handleApi { apiServices.getBdtXml(documentId) }

    override suspend fun getContentItems(documentName: String): NetworkResult<ContentItemsApiResponse> = handleApi { apiServices.getContentItems(documentName) }
}