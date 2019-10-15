package me.lambdatamer.kandroid.lifecycle

import androidx.annotation.MainThread
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import me.lambdatamer.kandroid.extensions.createLogger
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused", "PrivatePropertyName")
class SingleLiveEvent<T> : MutableLiveData<T>() {

    private val L = createLogger()

    private val isPending = AtomicBoolean(false)

    @MainThread
    override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
        if (hasActiveObservers()) {
            L.w("Multiple observers registered but only one will be notified of changes.")
        }

        super.observe(owner, Observer {
            if (isPending.compareAndSet(true, false)) observer.onChanged(it)
        })
    }

    @MainThread
    override fun setValue(t: T?) {
        isPending.set(true)
        super.setValue(t)
    }

    @MainThread
    fun callNow() {
        value = null
    }

    fun call() {
        postValue(null)
    }
}