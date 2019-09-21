package me.lambdatamer.kandroid.recycler

import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import me.lambdatamer.kandroid.extensions.className
import me.lambdatamer.kandroid.extensions.onRippleClick
import java.lang.ref.WeakReference

@Suppress("unused")
abstract class DelegatedAdapter<T> : RecyclerView.Adapter<AdapterDelegate.ViewHolder<T, *>>() {

    protected abstract val delegates: Set<AdapterDelegate<out T, *>>

    private var data: List<T> = emptyList()

    private var recyclerReference = WeakReference<RecyclerView>(null)
    protected val attachedRecycler: RecyclerView? get() = recyclerReference.get()

    override fun getItemCount() = data.size

    open fun onDataSetChanged(data: List<T>) {}

    open fun onItemClick(item: T, binding: ViewDataBinding, delegate: AdapterDelegate<T, *>) {}

    fun setData(newData: List<T>) {
        data = newData
        notifyDataSetChanged()
        onDataSetChanged(newData)
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]!!
        val delegate = delegates.find { it.isFor(item) }
        requireNotNull(delegate) { "AdapterDelegate is not registered for class ${item.className}" }
        return delegate.viewType
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDelegate.ViewHolder<T, *> {
        val delegate = requireNotNull(delegates.find { it.viewType == viewType })
        return delegate.createViewHolder(parent) as AdapterDelegate.ViewHolder<T, *>
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: AdapterDelegate.ViewHolder<T, *>, position: Int) {
        val item = data[position]!!
        val delegate = requireNotNull(delegates.find { it.isFor(item) })
                as AdapterDelegate<T, AdapterDelegate.ViewHolder<T, *>>
        delegate.bind(holder, item)
        if (delegate.isClickable) holder.itemView.onRippleClick {
            onItemClick(item, holder.binding, delegate)
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerReference = WeakReference(recyclerView)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerReference = WeakReference<RecyclerView>(null)
    }

}

