package me.lambdatamer.kandroid.recycler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.IdRes
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

@Suppress("unused")
abstract class AdapterDelegate<in TItem, TViewHolder : AdapterDelegate.ViewHolder<TItem, *>>(
    private val itemClass: Class<TItem>
) {

    internal val viewType = ViewCompat.generateViewId()

    open val isClickable: Boolean = false

    abstract fun createViewHolder(parent: ViewGroup): TViewHolder

    open fun areItemsTheSame(oldItem: TItem, newItem: TItem) = true

    open fun areContentsTheSame(oldItem: TItem, newItem: TItem) = oldItem == newItem

    internal fun bind(viewHolder: TViewHolder, item: TItem) = viewHolder.bind(item)

    internal fun isFor(item: Any) = item::class.java == itemClass

    abstract class ViewHolder<in T, out B : ViewDataBinding>(
        parent: ViewGroup,
        inflater: (LayoutInflater, ViewGroup, Boolean) -> B,
        val binding: B = inflater(LayoutInflater.from(parent.context), parent, false)
    ) : RecyclerView.ViewHolder(binding.root) {

        val context = checkNotNull(itemView.context)

        abstract fun bind(item: T)

        protected fun <T : View> findViewById(@IdRes id: Int) = itemView.findViewById<T>(id)!!

        protected fun getColor(@ColorRes id: Int) = ContextCompat.getColor(itemView.context, id)
    }
}