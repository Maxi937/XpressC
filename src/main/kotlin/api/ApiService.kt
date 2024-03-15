package api


import api.models.BdtXmlApiResponse
import api.models.ContentItemsApiResponse
import api.models.NetworkResult
import api.models.XpressionDocumentModelApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("/api/xpression/models")
    suspend fun getDocumentModels(@Query("env") env: String): Response<XpressionDocumentModelApiResponse>

    @GET("/api/xpression/{documentName}/bdt")
    suspend fun getBdtXml(@Path("documentName") documentName: String): Response<BdtXmlApiResponse>

    @GET("/api/xpression/{id}/bdt")
    suspend fun getBdtXml(@Path("id") documentId: Long): Response<BdtXmlApiResponse>

    @GET("/api/xpression/{documentName}/content")
    suspend fun getContentItems(@Path("documentName") documentName: String): Response<ContentItemsApiResponse>
}