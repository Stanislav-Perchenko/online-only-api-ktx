package cam.alperez.samples.onlineonlyapiktx.ui.common

import java.util.*

class ErrorUiMessage(val displayText: String, val detailedDescription: String) {

    override fun toString() = String.format(
            Locale.UK,
            "ErrorUiMessage{displayText='%s', detailedDescription='%s'",
            displayText,
            detailedDescription
        )
}