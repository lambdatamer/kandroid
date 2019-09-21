@file:Suppress("unused")

package me.lambdatamer.kandroid.databinding

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions

@BindingAdapter("loadImageUrl", "placeholder", "circleCrop", requireAll = false)
fun bindingLoadImageUrl(
    imageView: ImageView,
    url: String?,
    placeholder: Drawable?,
    circleCrop: Boolean?
) {
    if (url != null) {
        Glide.with(imageView.context)
            .load(url)
            .transition(DrawableTransitionOptions.withCrossFade())
            .run { if (placeholder != null) placeholder(placeholder) else this }
            .run { if (circleCrop == true) apply(RequestOptions.circleCropTransform()) else this }
            .into(imageView)
    } else {
        imageView.setImageDrawable(placeholder)
    }
}
