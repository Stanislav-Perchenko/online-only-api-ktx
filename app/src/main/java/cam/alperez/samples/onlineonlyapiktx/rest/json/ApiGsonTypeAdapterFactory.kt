package cam.alperez.samples.onlineonlyapiktx.rest.json

import cam.alperez.samples.onlineonlyapiktx.utils.FileType
import cam.alperez.samples.onlineonlyapiktx.utils.IntId
import com.google.gson.Gson
import com.google.gson.TypeAdapter
import com.google.gson.TypeAdapterFactory
import com.google.gson.reflect.TypeToken
import java.net.URL


class ApiGsonTypeAdapterFactory : TypeAdapterFactory {

    override fun <T> create(gson: Gson, type: TypeToken<T>): TypeAdapter<T>? {
        val cls = type.rawType
        return when {
            (cls == IntId::class.java) -> IntIdTypeAdapter() as TypeAdapter<T>
            (cls == FileType::class.java) -> FileTypeTypeAdapter() as TypeAdapter<T>
            (cls == URL::class.java) -> URLTypeAdapter() as TypeAdapter<T>
            else -> null
        }
    }
}