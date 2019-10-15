package me.lambdatamer.kandroid.extensions

import me.lambdatamer.kandroid.log.KLogger

val Any?.className get() = if (this != null) this::class.java.simpleName else "[null]"

fun Any.createLogger(
    debug: Boolean = false,
    messageFormatter: (Any?) -> String = KLogger.DEFAULT_FORMAT
) = KLogger(className, debug, messageFormatter)