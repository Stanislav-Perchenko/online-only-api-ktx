package cam.alperez.samples.onlineonlyapiktx.rest

import androidx.lifecycle.LiveData
import cam.alperez.samples.onlineonlyapiktx.BuildConfig
import cam.alperez.samples.onlineonlyapiktx.entity.BookEntity
import cam.alperez.samples.onlineonlyapiktx.entity.CategoryEntity
import cam.alperez.samples.onlineonlyapiktx.rest.json.ApiGsonTypeAdapterFactory
import cam.alperez.samples.onlineonlyapiktx.rest.utils.ApiResponse
import cam.alperez.samples.onlineonlyapiktx.rest.utils.LiveDataRetrofitCallAdapterFactory
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.concurrent.TimeUnit

interface ApplicationRestService {
    companion object {

        private val httpClientParams: HttpClientSetupParams = HttpClientSetupParams.Builder()
            .setMaxIdleConnections(3)
            .setKeepAliveDuration(60)
            .setKeepAliveTimeUnit(TimeUnit.SECONDS)
            .setConnectTimeout(15)
            .setConnectTimeoutTimeUnit(TimeUnit.SECONDS)
            .setReadTimeout(20)
            .setReadTimeoutTimeUnit(TimeUnit.SECONDS)
            .setWriteTimeout(10)
            .setWriteTimeoutTimeUnit(TimeUnit.SECONDS)
            .setHttpLoggingLevelOnDebug(HttpLoggingInterceptor.Level.BODY)
            .setUpstreamUseGzip(!BuildConfig.DEBUG)
            .setHeaderUserAgent("Stanislav.Perchenko")
            .setHeaderAccept("*/*")
            .setHeaderAcceptEncoding("gzip, deflate, br")
            .setHeaderConnection("keep-alive")
            .setHeaderContentType("application/json; charset=UTF-8")
            .build()
        private val customGson: Gson = GsonBuilder()
            .registerTypeAdapterFactory(ApiGsonTypeAdapterFactory())
            .create()

        val INSTANCE: ApplicationRestService = Retrofit.Builder()
            .baseUrl(BuildConfig.BACKEND_API_URL)
            .client(HttpClientProvider.forParameters(httpClientParams).httpClient)
            .addConverterFactory(GsonConverterFactory.create(customGson))
            .addCallAdapterFactory(LiveDataRetrofitCallAdapterFactory())
            .build()
            .create<ApplicationRestService>(ApplicationRestService::class.java)
    }

    @GET("/v3/3fd5ae30-670f-4de0-85ba-71bcc230c3e4")
    fun getCategories(): LiveData<ApiResponse<List<CategoryEntity>>>

    @GET("{api_link}")
    fun getBooksForCategory(@Path(value = "api_link", encoded = true) apiLink: String?): LiveData<ApiResponse<List<BookEntity>>>
}