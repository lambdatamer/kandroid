package me.lambdatamer.kandroid.extensions

import android.view.View
import androidx.core.os.postDelayed

private const val RIPPLE_CLICK_DELAY = 150L

fun View.onClick(listener: (View) -> Unit) = setOnClickListener(listener)

fun View.onRippleClick(delay: Long = RIPPLE_CLICK_DELAY, listener: (View) -> Unit) = onClick {
    handler.postDelayed(delay) { listener(this) }
}
