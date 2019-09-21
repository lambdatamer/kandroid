package me.lambdatamer.kandroid.log

import android.util.Log

@Suppress("unused", "MemberVisibilityCanBePrivate")
class KLogger(
    private val tag: String,
    private val debug: Boolean = false,
    private val messageFormatter: (any: Any?) -> String = DEFAULT_FORMAT
) {

    companion object {
        internal val DEFAULT_FORMAT = { any: Any? -> "[${Thread.currentThread().name}] $any" }
    }

    // Verbose
    fun v(throwable: Throwable) = v("", throwable)

    fun v(any: Any?) = Log.v(tag, messageFormatter(any))

    fun v(any: Any?, throwable: Throwable) = Log.v(tag, messageFormatter(any), throwable)

    // Debug
    fun d(throwable: Throwable) = d("", throwable)

    fun d(any: Any?) = Log.d(tag, messageFormatter(any))

    fun d(any: Any?, throwable: Throwable) = Log.d(tag, messageFormatter(any), throwable)

    // Info
    fun i(throwable: Throwable) = i("", throwable)

    fun i(any: Any?) = Log.i(tag, messageFormatter(any))

    fun i(any: Any?, throwable: Throwable) = Log.i(tag, messageFormatter(any), throwable)

    // Warning
    fun w(throwable: Throwable) = w("", throwable)

    fun w(any: Any?) = Log.w(tag, messageFormatter(any))

    fun w(any: Any?, throwable: Throwable) = Log.w(tag, messageFormatter(any), throwable)

    // Error
    fun e(throwable: Throwable) = e("", throwable)

    fun e(any: Any?) = Log.e(tag, messageFormatter(any))

    fun e(any: Any?, throwable: Throwable) = Log.e(tag, messageFormatter(any), throwable)

    // Assert
    fun a(any: Any?, e: Throwable? = null) {
        if (debug) {
            throw AssertionError(messageFormatter(any), e)
        } else {
            Log.wtf(tag, messageFormatter(any), e)
        }
    }

}