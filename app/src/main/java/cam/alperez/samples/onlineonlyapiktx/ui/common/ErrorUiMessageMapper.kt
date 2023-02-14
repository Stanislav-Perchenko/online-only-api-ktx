package cam.alperez.samples.onlineonlyapiktx.ui.common

import android.content.Context
import android.content.res.Resources
import cam.alperez.samples.onlineonlyapiktx.rest.utils.ApiResponse
import cam.alperez.samples.onlineonlyapiktx.R
import com.google.gson.stream.MalformedJsonException
import java.io.IOException
import java.util.*
import java.util.function.Function

class ErrorUiMessageMapper(context: Context) : Function<ApiResponse<*>, ErrorUiMessage> {
    private val resources: Resources

    init {
        resources = context.resources
    }

    override fun apply(apiResponse: ApiResponse<*>): ErrorUiMessage {
        val displayText: String
        val description: String
        val localError: Throwable? = apiResponse.getLocalError()

        if (apiResponse.isSuccessful()) {
            displayText = resources.getString(R.string.result_success)
            description = String.format(
                Locale.UK,
                "%d - %s",
                apiResponse.httpCode(),
                apiResponse.httpMessage()
            )
        } else if (localError == null) {
            displayText = resources.getString(R.string.result_server_error)
            description = String.format(
                Locale.UK,
                "%d - %s",
                apiResponse.httpCode(),
                apiResponse.httpMessage()
            )
        } else if (localError is MalformedJsonException) {
            displayText = resources.getString(R.string.result_server_bad_data)
            description = localError.message ?: ""
        } else if (localError is IOException) {
            displayText = resources.getString(R.string.result_connection_error)
            description =
                localError.javaClass.simpleName + ": " + localError.message
        } else {
            displayText = resources.getString(R.string.result_critical_app_error)
            description =
                localError.javaClass.simpleName + ": " + localError.message
        }
        return ErrorUiMessage(displayText, description)
    }
}