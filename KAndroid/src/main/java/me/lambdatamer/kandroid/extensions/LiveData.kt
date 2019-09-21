@file:Suppress("unused")

package me.lambdatamer.kandroid.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

fun <T> MutableLiveData<T>.immutable(): LiveData<T> = this

fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = Transformations.distinctUntilChanged(this)

fun <T> LiveData<T?>.filterNotNull(): LiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this) { if (it != null) mediator.value = it }
}

fun <T, R> LiveData<T>.map(mapper: (T) -> R): LiveData<R> = Transformations.map(this, mapper)

fun <T, R> LiveData<T>.mapNotNull(mapper: (T) -> R?): LiveData<R> = map(mapper).filterNotNull()

fun <T1, T2, R> LiveData<T1>.zip(with: LiveData<T2>, zipper: (T1, T2) -> R): LiveData<R> =
    MediatorLiveData<R>().also { mediator ->
        fun update(left: T1?, right: T2?) {
            if (left != null && right != null) mediator.value = zipper(left, right)
        }
        mediator.addSource(this) { left -> update(left, with.value) }
        mediator.addSource(with) { right -> update(this.value, right) }
    }

fun <T1, T2, R> LiveData<T1>.zipNotNull(with: LiveData<T2>, zipper: (T1, T2) -> R?): LiveData<R> =
    zip(with, zipper).filterNotNull()

fun <T, R> LiveData<T>.switchMap(switcher: (T) -> LiveData<R>?) =
    Transformations.switchMap(this, switcher)

fun <T, R> LiveData<T>.switchMapNotNull(switcher: (T) -> LiveData<R>?) =
    switchMap(switcher).filterNotNull()

fun <T1, T2, R> LiveData<T1>.merge(with: LiveData<T2>, merger: (T1?, T2?) -> R?): LiveData<R?> =
    MediatorLiveData<R>().also { mediator ->
        fun update(left: T1?, right: T2?) {
            mediator.value = merger(left, right)
        }
        mediator.addSource(this) { left -> update(left, with.value) }
        mediator.addSource(with) { right -> update(this.value, right) }
    }

fun <T1, T2, R> LiveData<T1>.mergeNotNull(with: LiveData<T2>, merger: (T1?, T2?) -> R?): LiveData<R> =
    merge(with, merger).filterNotNull()