@file:Suppress("unused")

package me.lambdatamer.kandroid.extensions

import android.os.Bundle
import android.os.Parcelable

private const val KEY_PACK = "bundle_pack"

fun Parcelable.pack() = Bundle().pack(this)

fun Bundle.pack(parcelable: Parcelable) = put(KEY_PACK, parcelable)

fun <T : Parcelable> Bundle.unpack() = requireNotNull(getParcelable<T>(KEY_PACK))

fun Bundle.put(key: String, value: Parcelable) = apply { putParcelable(key, value) }