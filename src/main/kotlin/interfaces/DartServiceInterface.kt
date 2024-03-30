package interfaces

import api.models.BdtXmlApiResponse
import api.models.ContentGroupApiResponse
import api.models.NetworkResult
import api.models.XpressionDocumentModelApiResponse


interface DartServiceInterface {
    suspend fun getDocumentModels(env: String): NetworkResult<XpressionDocumentModelApiResponse>

    suspend fun getBdtXml(documentName: String, env: String): NetworkResult<BdtXmlApiResponse>
    suspend fun getBdtXml(documentId: Long, env: String): NetworkResult<BdtXmlApiResponse>

    suspend fun getContentGroup(
        documentName: String,
        textClassId: Int,
        env: String
    ): NetworkResult<ContentGroupApiResponse>

}

