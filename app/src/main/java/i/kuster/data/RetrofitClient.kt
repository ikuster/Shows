package i.kuster.data

import android.util.Log
import i.kuster.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.ResponseBody
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import java.io.IOException

object RetrofitClient {
    private var retrofit: Retrofit? = null

    private const val BASE_URL = "https://api.infinum.academy/api/"
    var httpClient: OkHttpClient.Builder = OkHttpClient.Builder().addInterceptor(LogJsonInterceptor())

    val retrofitInstance: Retrofit?
        get() {
            if (retrofit == null) {
                retrofit = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(MoshiConverterFactory.create())
                    .client(httpClient.build())
                    .build()
            }
            return retrofit
        }

    class LogJsonInterceptor : Interceptor {
        @Throws(IOException::class)
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()

            val response = chain.proceed(request)
            val rawJson = response.body()?.string()

            Log.d(BuildConfig.APPLICATION_ID, String.format("raw JSON response is: %s", rawJson))

            // Re-create the response before returning it because body can be read only once
            return response.newBuilder()
                .body(ResponseBody.create(response.body()?.contentType(), rawJson)).build()
        }
    }

}

