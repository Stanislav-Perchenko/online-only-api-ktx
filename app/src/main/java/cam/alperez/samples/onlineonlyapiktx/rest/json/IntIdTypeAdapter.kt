package cam.alperez.samples.onlineonlyapiktx.rest.json

import cam.alperez.samples.onlineonlyapiktx.entity.Entity
import com.google.gson.TypeAdapter
import cam.alperez.samples.onlineonlyapiktx.utils.IntId
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.io.IOException
import kotlin.Throws

internal class IntIdTypeAdapter : TypeAdapter<IntId<out Entity<*>>>() {
    @Throws(IOException::class)
    override fun write(out: JsonWriter, value: IntId<out Entity<*>>?) {
        if (value == null) {
            out.nullValue()
        } else {
            out.value(value.idValue.toLong())
        }
    }

    @Throws(IOException::class)
    override fun read(input: JsonReader): IntId<out Entity<*>>? {
        return if (input.peek() == JsonToken.NULL) {
            input.nextNull()
            null
        } else {
            val id = input.nextInt()
            IntId.valueOf(id)
        }
    }
}