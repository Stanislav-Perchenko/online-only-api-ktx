package cam.alperez.samples.onlineonlyapiktx.rest.utils

import androidx.lifecycle.LiveData
import cam.alperez.samples.onlineonlyapiktx.rest.utils.ApiResponse.Companion.create
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Callback
import retrofit2.Response
import java.lang.reflect.Type
import java.util.concurrent.atomic.AtomicBoolean

class LiveDataRetrofitCallAdapter<T>(private val respType: Type) :
    CallAdapter<T, LiveData<ApiResponse<T>>> {

    override fun responseType(): Type {
        return respType
    }

    override fun adapt(call: Call<T>): LiveData<ApiResponse<T>> {
        return object : LiveData<ApiResponse<T>>() {
            private val isStarted = AtomicBoolean(false)

            override fun onActive() {
                super.onActive()
                if (isStarted.compareAndSet(false, true)) {
                    call.enqueue(object : Callback<T> {
                        override fun onResponse(c: Call<T>, response: Response<T>) {
                            val apiResp = create(response)
                            postValue(apiResp)
                        }

                        override fun onFailure(c: Call<T>, t: Throwable) {
                            val apiResp = create<T>(t)
                            postValue(apiResp)
                        }
                    })
                }
            }
        }
    }
}