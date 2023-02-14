package cam.alperez.samples.onlineonlyapiktx.rest.json

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.net.URL
import kotlin.Throws

internal class URLTypeAdapter : TypeAdapter<URL?>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: URL?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.toString())
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): URL? {
        return if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            val value = input.nextString().trim { it <= ' ' }
            if (value.isEmpty()) {
                throw IOException("Cannot deserialize URL: value is empty")
            }

            //This throws MalformedURLException in case of error, so we are good with this.
            URL(value)
        }
    }
}