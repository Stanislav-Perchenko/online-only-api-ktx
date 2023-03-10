package cam.alperez.samples.onlineonlyapiktx.rest.utils

import androidx.lifecycle.LiveData
import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class LiveDataRetrofitCallAdapterFactory : CallAdapter.Factory() {
    override fun get(
        returnType: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): CallAdapter<*, *>? {
        if (getRawType(returnType) != LiveData::class.java) {
            return null
        }

        val observableType = getParameterUpperBound(0, returnType as ParameterizedType)
        require(observableType is ParameterizedType) { "resource must be parameterized" }

        val rawObservableType = getRawType(observableType)
        require(rawObservableType == ApiResponse::class.java) { "type must be a resource" }

        val bodyType = getParameterUpperBound(0, observableType)
        return LiveDataRetrofitCallAdapter<Any>(bodyType)
    }
}