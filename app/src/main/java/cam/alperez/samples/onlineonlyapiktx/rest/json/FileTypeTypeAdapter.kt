package cam.alperez.samples.onlineonlyapiktx.rest.json

import com.google.gson.TypeAdapter
import cam.alperez.samples.onlineonlyapiktx.utils.FileType
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import java.lang.Exception
import java.util.*
import kotlin.Throws

internal class FileTypeTypeAdapter : TypeAdapter<FileType?>() {

    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: FileType?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.name.lowercase(Locale.UK))
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): FileType? {
        return if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            val value = input.nextString().uppercase(Locale.UK)
            try {
                FileType.valueOf(value)
            } catch (e: Exception) {
                throw IOException(
                    String.format(
                        "Cannot deserialize FileType: no such value (%s)",
                        value
                    )
                )
            }
        }
    }
}