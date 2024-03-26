package api


import api.models.BdtXmlApiResponse
import api.models.ContentGroupApiResponse
import api.models.XpressionDocumentModelApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiServices {
    @GET("/api/xpression/models")
    suspend fun getDocumentModels(@Query("env") env: String): Response<XpressionDocumentModelApiResponse>

    @GET("/api/xpression/bdt")
    suspend fun getBdtXml(
        @Query("documentName") documentName: String,
        @Query("env") env: String
    ): Response<BdtXmlApiResponse>

    @GET("/api/xpression/bdt")
    suspend fun getBdtXml(@Query("documentId") documentId: Long, @Query("env") env: String): Response<BdtXmlApiResponse>

    @GET("/api/xpression/{documentName}/content")
    suspend fun getContentGroup(
        @Path("documentName") documentName: String,
        @Query("textClassId") textClassId: Int,
        @Query("env") env: String
    ): Response<ContentGroupApiResponse>
}