package cam.alperez.samples.onlineonlyapiktx.ui.common

open class ApiListResponseUiBundle<T>(
    val isLoading: Boolean,
    val isSuccess: Boolean,
    val data: List<T>?,
    val error: ErrorUiMessage?,
    val isErrorMessageShow: Boolean
) {
    open fun withIsLoading(newIsLoading: Boolean): ApiListResponseUiBundle<T> {
        return ApiListResponseUiBundle(newIsLoading, isSuccess, data, error, isErrorMessageShow)
    }

    open fun withErrorShow(newIsErrShow: Boolean): ApiListResponseUiBundle<T> {
        return ApiListResponseUiBundle(isLoading, isSuccess, data, error, newIsErrShow)
    }

    companion object {

        fun <S> createSuccess(data: List<S>?): ApiListResponseUiBundle<S> {
            return ApiListResponseUiBundle(isLoading = false, isSuccess = true, data, null, false)
        }

        fun <S> createError(err: ErrorUiMessage?): ApiListResponseUiBundle<S> {
            return ApiListResponseUiBundle(isLoading = false, isSuccess = false, null, err, true)
        }
    }
}