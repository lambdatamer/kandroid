package me.lambdatamer.kandroid.atom

sealed class Atom<T> {
    class Loading<T> : Atom<T>()
    data class Success<T>(val content: T) : Atom<T>()
    data class Error<T>(val throwable: Throwable) : Atom<T>()
}