@file:Suppress("unused")

package me.lambdatamer.kandroid.databinding

import android.view.View
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import me.lambdatamer.kandroid.atom.Atom

@BindingAdapter("visibleIfAtomLoading")
fun visibleIfAtomLoading(view: View, result: LiveData<*>) {
    view.isVisible = (result.value as? Atom<*>?) is Atom.Loading
}

@BindingAdapter("visibleIfAtomSuccess")
fun visibleIfAtomSuccess(view: View, result: LiveData<*>) {
    view.isVisible = (result.value as? Atom<*>) is Atom.Success
}

@BindingAdapter("visibleIfAtomError")
fun visibleIfAtomError(view: View, result: LiveData<*>) {
    view.isVisible = (result.value as? Atom<*>) is Atom.Error
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("visibleIfAtomNotEmpty")
fun visibleIfAtomNotEmpty(view: View, result: LiveData<*>) {
    view.isVisible = (result as? LiveData<Atom<Collection<*>>>)?.value?.let { atom ->
        atom is Atom.Success && atom.content.isNotEmpty()
    } ?: false
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("visibleIfAtomEmpty")
fun visibleIfAtomEmpty(view: View, result: LiveData<*>) {
    view.isVisible = (result as? LiveData<Atom<Collection<*>>>)?.value?.let { atom ->
        atom is Atom.Success && atom.content.isEmpty()
    } ?: false
}

@Suppress("UNCHECKED_CAST")
@BindingAdapter("refreshingIfAtomLoading")
fun refreshingIfAtomLoading(view: SwipeRefreshLayout, result: LiveData<*>) {
    view.isRefreshing = (result as? LiveData<Atom<*>>)?.value is Atom.Loading
}
