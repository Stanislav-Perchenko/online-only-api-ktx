package cam.alperez.samples.onlineonlyapiktx.rest.utils

import retrofit2.Response

interface ApiResponse<T> {
    fun isSuccessful(): Boolean
    fun getLocalError(): Throwable?
    fun httpCode(): Int
    fun httpMessage(): String?
    fun getResponseData(): T?

    companion object {
        fun <R> create(response: Response<R>): ApiResponse<R> {
            return RemoteApiResponseImpl(response.code(), response.message(), response.body())
        }

        fun <R> create(t: Throwable): ApiResponse<R> {
            return LocalErrorApiResponseImpl(t)
        }
    }
}