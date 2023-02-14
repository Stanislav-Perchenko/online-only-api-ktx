package cam.alperez.samples.onlineonlyapiktx.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData

class MapMutableLiveData<T> private constructor() : MediatorLiveData<T>() {
    companion object {
        fun <X, Y> create(
            source: LiveData<X>,
            mapFunction: (X) -> Y
        ): MapMutableLiveData<Y> {
            val instance: MapMutableLiveData<Y> = MapMutableLiveData()
            instance.addSource(source) { x: X ->
                instance.setValue(
                    mapFunction(x)
                )
            }
            return instance
        }
    }
}