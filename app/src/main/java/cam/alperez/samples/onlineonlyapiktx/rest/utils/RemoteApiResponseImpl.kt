package cam.alperez.samples.onlineonlyapiktx.rest.utils

internal class RemoteApiResponseImpl<T>(
    private val httpResponseCode: Int,
    private val httpResponseMessage: String,
    private val data: T?
) : ApiResponse<T> {
    override fun isSuccessful(): Boolean {
        return httpResponseCode in 200..299
    }

    override fun getLocalError(): Throwable? {
        return null
    }

    override fun httpCode(): Int {
        return httpResponseCode
    }

    override fun httpMessage(): String {
        return httpResponseMessage
    }

    override fun getResponseData(): T? {
        return data
    }
}