package cam.alperez.samples.onlineonlyapiktx.rest.utils

internal class LocalErrorApiResponseImpl<T>(private val error: Throwable) : ApiResponse<T> {
    override fun isSuccessful(): Boolean {
        return false
    }

    override fun getLocalError(): Throwable? {
        return error
    }

    override fun httpCode(): Int {
        return 0
    }

    override fun httpMessage(): String? {
        return null
    }

    override fun getResponseData(): T? {
        return null
    }
}