package api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//"http://localhost:4000"
//"https://myfitnesstrainer-backend.onrender.com"
object DartClient {
    var TOKEN = ""

//    private val okHttpClient = OkHttpClient.Builder()
//        .addInterceptor { chain ->
//            var request = chain.request()
//            request = request.newBuilder().header("Cache-Control", "public, max-age=" + 5).build()
//            chain.proceed(request)
//        }
//        .addInterceptor { chain ->
//            val request =
//                chain.request().newBuilder().addHeader("Authorization", "Bearer $TOKEN").build()
//            chain.proceed(request)
//        }
//        .build()

    private val api: ApiServices by lazy {
        Retrofit.Builder()
            .baseUrl("http://localhost:4000")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiServices::class.java)
    }

    val service: DartService = DartServiceImpl(api)
}