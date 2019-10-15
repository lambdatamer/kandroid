package me.lambdatamer.kandroid.extensions

inline fun <T> List<T>.findAndReplace(with: T, predicate: (T) -> Boolean): Pair<List<T>, Boolean> {
    var isFound = false
    return map {
        if (predicate(it)) with.also { isFound = true }
        else it
    } to isFound
}