package cam.alperez.samples.onlineonlyapiktx.utils

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.LiveData
import androidx.annotation.MainThread
import androidx.lifecycle.Observer
import java.lang.RuntimeException

class ReplaceableSourceMediatorLiveData<T> : MediatorLiveData<T>() {
    private var source: LiveData<*>? = null

    @MainThread
    fun setSource(source: LiveData<T>) {
        this.source?.also {
            super.removeSource(it)
        }
        this.source = source
        super.addSource(source) { value: T -> this.setValue(value) }
    }

    override fun <S> addSource(source: LiveData<S>, onChanged: Observer<in S>) {
        throw RuntimeException("Call not supported")
    }

    override fun <S> removeSource(toRemote: LiveData<S>) {
        throw RuntimeException("Call not supported")
    }
}