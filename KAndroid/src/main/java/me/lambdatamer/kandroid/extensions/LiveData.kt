@file:Suppress("unused")

package me.lambdatamer.kandroid.extensions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations

val <T : Any> LiveData<T>.requireValue get() = requireNotNull(value)

fun <T> MutableLiveData<T>.immutable(): LiveData<T> = this

fun <T> LiveData<T>.mutable(): MutableLiveData<T> = this as MutableLiveData<T>


fun <T> LiveData<T>.distinctUntilChanged(): LiveData<T> = Transformations.distinctUntilChanged(this)

fun <T> LiveData<T>.filterNotNull(): LiveData<T> = MediatorLiveData<T>().also { mediator ->
    mediator.addSource(this) { if (it != null) mediator.value = it }
}


fun <T, R> LiveData<T>.map(mapper: (T?) -> R): LiveData<R> = Transformations.map(this, mapper)

/**
 * Will ignore null values returned from [mapper]
 */
fun <T, R> LiveData<T>.mapNotNull(mapper: (T?) -> R): LiveData<R> = map(mapper).filterNotNull()


fun <T1, T2, R> LiveData<T1>.zip(with: LiveData<T2>, zipper: (T1?, T2?) -> R): LiveData<R> =
    MediatorLiveData<R>().also { mediator ->
        fun update(left: T1?, right: T2?) {
            if (left != null && right != null) mediator.value = zipper(left, right)
        }
        mediator.addSource(this) { left -> update(left, with.value) }
        mediator.addSource(with) { right -> update(this.value, right) }
    }

/**
 * Will ignore null values returned from [zipper]
 */
fun <T1, T2, R> LiveData<T1>.zipNotNull(with: LiveData<T2>, zipper: (T1?, T2?) -> R): LiveData<R> =
    zip(with, zipper).filterNotNull()


fun <T, R, L : LiveData<R>> LiveData<T>.switchMap(mapper: (T?) -> L) =
    Transformations.switchMap(this, mapper)

/**
 * Will ignore null values returned from [mapper]
 */
fun <T, R, L : LiveData<R>> LiveData<T>.switchMapNotNull(mapper: (T?) -> L) =
    switchMap(mapper).filterNotNull()


fun <T1, T2, R> LiveData<T1>.merge(with: LiveData<T2>, merger: (T1?, T2?) -> R): LiveData<R> =
    MediatorLiveData<R>().also { mediator ->
        fun update(left: T1?, right: T2?) {
            mediator.value = merger(left, right)
        }
        mediator.addSource(this) { left -> update(left, with.value) }
        mediator.addSource(with) { right -> update(this.value, right) }
    }

/**
 * Will ignore null values returned from [merger]
 */
fun <T1, T2, R> LiveData<T1>.mergeNotNull(
    with: LiveData<T2>,
    merger: (T1?, T2?) -> R
): LiveData<R> = merge(with, merger).filterNotNull()