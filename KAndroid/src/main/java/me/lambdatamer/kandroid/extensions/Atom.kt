package me.lambdatamer.kandroid.extensions

import androidx.lifecycle.LiveData
import me.lambdatamer.kandroid.atom.Atom

inline fun <T, R> Atom<T>.map(mapper: (T) -> R): Atom<R> = when (this) {
    is Atom.Success -> Atom.Success(mapper(content))
    is Atom.Loading -> Atom.Loading()
    is Atom.Error -> Atom.Error(throwable)
}

fun <T, R> LiveData<Atom<T>>.mapAtom(mapper: (T) -> R) = mapNotNull { it?.map(mapper) }

fun <T, R> LiveData<Atom<T>>.flatMapSuccess(mapper: (T) -> R) =
    mapNotNull { if (it is Atom.Success) mapper(it.content) else null }

fun <T> LiveData<Atom<T>>.flatMapSuccess() = flatMapSuccess { it }