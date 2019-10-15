package me.lambdatamer.kandroid.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.core.content.withStyledAttributes
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.BindingAdapter
import androidx.lifecycle.LiveData
import me.lambdatamer.kandroid.R
import me.lambdatamer.kandroid.atom.Atom
import me.lambdatamer.kandroid.databinding.ViewLoadingButtonBinding
import me.lambdatamer.kandroid.extensions.loadStateListAnimator

@BindingAdapter("isLoading")
fun bindingIsLoading(view: LoadingButton, isLoading: Boolean) {
    view.isLoading = isLoading
}

@BindingAdapter("loadingIfAtomLoading")
fun bindingLoadingIfAtomLoading(view: LoadingButton, isLoading: LiveData<*>) {
    view.isLoading = (isLoading.value as? Atom<*>) is Atom.Loading
}

class LoadingButton @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {
    val binding: ViewLoadingButtonBinding =
        ViewLoadingButtonBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)

        context.withStyledAttributes(
            attrs,
            intArrayOf(android.R.attr.text),
            defStyleAttr,
            defStyleRes
        ) {
            binding.buttonText.text = getString(0)
            stateListAnimator =
                context.loadStateListAnimator(R.animator.loading_button_elevation)
        }
    }

    var isLoading: Boolean
        get() = binding.progress.isVisible
        set(value) = setIsLoading(value)

    private fun setIsLoading(isLoading: Boolean) {
        binding.progress.isVisible = isLoading
        binding.buttonText.isGone = isLoading
        isEnabled = !isLoading
    }
}