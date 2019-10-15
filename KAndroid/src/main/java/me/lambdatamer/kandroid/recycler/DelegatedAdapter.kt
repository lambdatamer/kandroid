package me.lambdatamer.kandroid.recycler

import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import me.lambdatamer.kandroid.extensions.className
import me.lambdatamer.kandroid.extensions.onRippleClick
import java.lang.ref.WeakReference

@Suppress("unused", "UNCHECKED_CAST")
abstract class DelegatedAdapter<T>(
    delegates: Set<AdapterDelegate<*, *>>
) : RecyclerView.Adapter<AdapterDelegate<T, *>.ViewHolder>() {

    private val delegates = delegates.map { it as AdapterDelegate<T, *> }.toSet()

    private var data: List<T> = emptyList()

    private var recyclerReference = WeakReference<RecyclerView>(null)
    protected val attachedRecycler: RecyclerView? get() = recyclerReference.get()

    override fun getItemCount() = data.size

    open fun onDataChanged(data: List<T>) {}

    open fun onItemClick(item: T, binding: ViewDataBinding, delegate: AdapterDelegate<T, *>) {}

    fun setData(newData: List<T>) {
        DiffUtil.calculateDiff(DiffUtilCallback(delegates, data, newData))
            .dispatchUpdatesTo(this)
        data = newData

        onDataChanged(newData)
    }

    override fun getItemViewType(position: Int): Int {
        val item = data[position]
        val delegate = delegates.find { it.isFor(item as Any) }
        requireNotNull(delegate) { "AdapterDelegate is not registered for class ${item.className}" }
        return delegate.viewType
    }

    @Suppress("UNCHECKED_CAST")
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AdapterDelegate<T, *>.ViewHolder {
        val delegate = requireNotNull(delegates.find { it.viewType == viewType })
        return delegate.createViewHolder(parent)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onBindViewHolder(holder: AdapterDelegate<T, *>.ViewHolder, position: Int) {
        val item = data[position]!!
        val delegate = requireNotNull(delegates.find { it.isFor(item) })
        holder.bind(item)
        if (delegate.isClickable) holder.itemView.onRippleClick {
            onItemClick(item, holder.binding, delegate)
        }
    }

    open fun getItemId(item: T): Long = -1

    final override fun getItemId(position: Int) = getItemId(data[position])

    @CallSuper
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        recyclerReference = WeakReference(recyclerView)
    }

    @CallSuper
    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        recyclerReference = WeakReference<RecyclerView>(null)
    }
}