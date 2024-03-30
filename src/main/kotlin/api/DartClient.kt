package api

import interfaces.ApiServices
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.Duration

//"http://localhost:4000"
//"http://0.0.0.0:10000"
object DartClient {
    var TOKEN = ""

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(Duration.ofSeconds(5))
        .build()

    private val api: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ApiServices::class.java)
    }

    val service: DartService = DartService(api)
}