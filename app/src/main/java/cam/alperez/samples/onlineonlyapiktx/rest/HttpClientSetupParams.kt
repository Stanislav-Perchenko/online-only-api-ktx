package cam.alperez.samples.onlineonlyapiktx.rest

import okhttp3.Interceptor
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

class HttpClientSetupParams private constructor(
    val maxIdleConnections: Int,
    val keepAliveDuration: Int,
    val keepAliveTimeUnit: TimeUnit,
    val connectTimeout: Int,
    val connectTimeoutTimeUnit: TimeUnit,
    val readTimeout: Int,
    val readTimeoutTimeUnit: TimeUnit,
    val writeTimeout: Int,
    val writeTimeoutTimeUnit: TimeUnit,
    val httpLoggingLevelOnDebug: HttpLoggingInterceptor.Level,      //HttpLoggingInterceptor.Level.BODY
    val upstreamUseGzip: Boolean,
    val headerUserAgent: String?,
    val headerAccept: String?,
    val headerAcceptEncoding: String?,
    val headerConnection: String?,
    val headerContentType: String?,
    val authInterceptor: Interceptor?) {

    data class Builder(
        var maxIdleConnections: Int = 0,
        var keepAliveDuration: Int = 0,
        var keepAliveTimeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        var connectTimeout: Int = 0,
        var connectTimeoutTimeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        var readTimeout: Int = 0,
        var readTimeoutTimeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        var writeTimeout: Int = 0,
        var writeTimeoutTimeUnit: TimeUnit = TimeUnit.MILLISECONDS,
        var httpLoggingLevelOnDebug: HttpLoggingInterceptor.Level = HttpLoggingInterceptor.Level.BODY,
        var upstreamUseGzip: Boolean = true,
        var headerUserAgent: String? = null,
        var headerAccept: String? = null,
        var headerAcceptEncoding: String? = null,
        var headerConnection: String? = null,
        var headerContentType: String? = null,
        var authInterceptor: Interceptor? = null) {
            fun setMaxIdleConnections(maxIdleConnections: Int) = apply { this.maxIdleConnections = maxIdleConnections }
            fun setKeepAliveDuration(keepAliveDuration: Int) = apply { this.keepAliveDuration = keepAliveDuration }
            fun setKeepAliveTimeUnit(keepAliveTimeUnit: TimeUnit) = apply { this.keepAliveTimeUnit = keepAliveTimeUnit }
            fun setConnectTimeout(connectTimeout: Int) = apply { this.connectTimeout = connectTimeout }
            fun setConnectTimeoutTimeUnit(connectTimeoutTimeUnit: TimeUnit) = apply { this.connectTimeoutTimeUnit = connectTimeoutTimeUnit }
            fun setReadTimeout(readTimeout: Int) = apply { this.readTimeout = readTimeout }
            fun setReadTimeoutTimeUnit(readTimeoutTimeUnit: TimeUnit) = apply { this.readTimeoutTimeUnit = readTimeoutTimeUnit }
            fun setWriteTimeout(writeTimeout: Int) = apply { this.writeTimeout = writeTimeout }
            fun setWriteTimeoutTimeUnit(writeTimeoutTimeUnit: TimeUnit) = apply { this.writeTimeoutTimeUnit = writeTimeoutTimeUnit }
            fun setHttpLoggingLevelOnDebug(httpLoggingLevelOnDebug: HttpLoggingInterceptor.Level) = apply { this.httpLoggingLevelOnDebug = httpLoggingLevelOnDebug }
            fun setUpstreamUseGzip(upstreamUseGzip: Boolean) = apply { this.upstreamUseGzip = upstreamUseGzip }
            fun setHeaderUserAgent(headerUserAgent: String?) = apply { this.headerUserAgent = headerUserAgent }
            fun setHeaderAccept(headerAccept: String?) = apply { this.headerAccept = headerAccept }
            fun setHeaderAcceptEncoding(headerAcceptEncoding: String?) = apply { this.headerAcceptEncoding = headerAcceptEncoding }
            fun setHeaderConnection(headerConnection: String?) = apply { this.headerConnection = headerConnection }
            fun setHeaderContentType(headerContentType: String?) = apply { this.headerContentType = headerContentType }
            fun setAuthInterceptor(authInterceptor: Interceptor?) = apply { this.authInterceptor = authInterceptor }

            fun build() = HttpClientSetupParams(maxIdleConnections, keepAliveDuration,keepAliveTimeUnit,connectTimeout,connectTimeoutTimeUnit,
                                                readTimeout,readTimeoutTimeUnit,writeTimeout,writeTimeoutTimeUnit,httpLoggingLevelOnDebug, upstreamUseGzip,
                                                headerUserAgent,headerAccept,headerAcceptEncoding,headerConnection,headerContentType,authInterceptor )
        }
}