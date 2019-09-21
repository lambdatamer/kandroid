@file:Suppress("unused")

package me.lambdatamer.kandroid.extensions

import org.kodein.di.KodeinAware
import org.kodein.di.description

val KodeinAware.kodeinBindingsDescription
    get() = "\n[ $className ]\n" + kodein.container.tree.bindings
        .filter { it.value.first().fromModule != "\u2063androidModule" }
        .description()