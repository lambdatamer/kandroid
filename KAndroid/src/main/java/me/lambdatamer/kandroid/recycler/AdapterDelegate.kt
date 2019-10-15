package me.lambdatamer.kandroid.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView.ViewHolder as RecyclerViewHolder

abstract class AdapterDelegate<in T, B : ViewDataBinding>(
    private val itemClass: Class<T>,
    private val bindingInflater: (LayoutInflater, ViewGroup, Boolean) -> B
) {
    internal val viewType = ViewCompat.generateViewId()

    open val isClickable: Boolean = false

    open fun areItemsTheSame(oldItem: T, newItem: T) = true

    open fun areContentsTheSame(oldItem: T, newItem: T) = oldItem == newItem

    abstract fun bind(viewHolder: ViewHolder, item: T)

    internal fun createViewHolder(parent: ViewGroup) = ViewHolder(parent)

    internal fun isFor(item: Any) = item::class.java == itemClass

    inner class ViewHolder internal constructor(
        parent: ViewGroup,
        val binding: B = bindingInflater(LayoutInflater.from(parent.context), parent, false)
    ) : RecyclerViewHolder(binding.root) {
        val context = checkNotNull(itemView.context)

        fun bind(item: T) = bind(this, item)
    }
}