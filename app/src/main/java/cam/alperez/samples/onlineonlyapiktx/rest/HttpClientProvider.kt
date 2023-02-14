package cam.alperez.samples.onlineonlyapiktx.rest

import cam.alperez.samples.onlineonlyapiktx.BuildConfig
import okhttp3.ConnectionPool
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.logging.HttpLoggingInterceptor
import java.io.IOException

class HttpClientProvider private constructor(private val params: HttpClientSetupParams) {

    private val mDefaultHeadersInterceptor = Interceptor { chain ->
        val r = chain.request()
        val rBld = r.newBuilder()
        val currHeaders = r.headers()
        if (currHeaders["Accept"] == null) {
            rBld.header("Accept", params.headerAccept)
        }
        if (currHeaders["Accept-Encoding"] == null) {
            rBld.header("Accept-Encoding", params.headerAcceptEncoding)
        }
        if (currHeaders["Connection"] == null) {
            rBld.header("Connection", params.headerConnection)
        }
        if (currHeaders["Content-Type"] == null) {
            rBld.header("Content-Type", params.headerContentType)
        }
        chain.proceed(rBld.build())
    }

    val httpClient: OkHttpClient
        get() {
            val protocols: MutableList<Protocol> = ArrayList(1)
            protocols.add(Protocol.HTTP_1_1)

            val cPool = ConnectionPool(
                params.maxIdleConnections,
                params.keepAliveDuration.toLong(),
                params.keepAliveTimeUnit
            )

            return OkHttpClient.Builder()
                .connectionPool(cPool)
                .protocols(protocols)
                .connectTimeout(params.connectTimeout.toLong(), params.connectTimeoutTimeUnit)
                .readTimeout(params.readTimeout.toLong(), params.readTimeoutTimeUnit)
                .writeTimeout(params.writeTimeout.toLong(), params.writeTimeoutTimeUnit)
                .addNetworkInterceptor { chain: Interceptor.Chain ->
                    chain.proceed(
                        chain.request().newBuilder()
                            .removeHeader("User-Agent")
                            .header("User-Agent", params.headerUserAgent)
                            .build()
                    )
                }
                .addNetworkInterceptor(mDefaultHeadersInterceptor)
                .addNetworkInterceptor(
                    HttpLoggingInterceptor().setLevel(
                        if (BuildConfig.DEBUG) params.httpLoggingLevelOnDebug else HttpLoggingInterceptor.Level.NONE
                    )
                )
                .addNetworkInterceptor { chain: Interceptor.Chain ->
                    try {
                        Thread.sleep(600)
                    } catch (e: InterruptedException) {
                        throw IOException(e.message, e)
                    }
                    chain.proceed(chain.request())
                }
                .build()
        }

    companion object {
        fun forParameters(parameters: HttpClientSetupParams) = HttpClientProvider(parameters)
    }
}