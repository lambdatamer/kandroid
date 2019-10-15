@file:Suppress("unused")

package me.lambdatamer.kandroid.databinding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import me.lambdatamer.kandroid.extensions.onRippleClick

@BindingAdapter("onRippleClick")
fun bindingOnRippleClick(view: View, listener: () -> Unit) {
    view.onRippleClick { listener() }
}

@BindingAdapter("android:visibility", "defaultVisibility", requireAll = false)
fun setVisibility(view: View, isVisible: Boolean?, defaultVisibility: Boolean?) {
    view.isVisible = isVisible ?: defaultVisibility ?: true
}
