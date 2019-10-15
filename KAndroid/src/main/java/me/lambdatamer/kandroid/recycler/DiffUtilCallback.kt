package me.lambdatamer.kandroid.recycler

import androidx.recyclerview.widget.DiffUtil

class DiffUtilCallback<T>(
    delegates: Set<AdapterDelegate<T, *>>,
    oldList: List<T>,
    newList: List<T>
) : DiffUtil.Callback() {

    private val oldItems = oldList.associateWithDelegate(delegates)
    private val newItems = newList.associateWithDelegate(delegates)

    @Suppress("UNCHECKED_CAST")
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, oldDelegate) = checkNotNull(oldItems[oldItemPosition])
        val (newItem, newDelegate) = checkNotNull(newItems[newItemPosition])

        return if (oldDelegate == newDelegate) {
            (newDelegate as AdapterDelegate<T, *>).areItemsTheSame(oldItem, newItem)
        } else {
            false
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val (oldItem, _) = checkNotNull(oldItems[oldItemPosition])
        val (newItem, delegate) = checkNotNull(newItems[newItemPosition])

        return (delegate as AdapterDelegate<T, *>).areContentsTheSame(oldItem, newItem)
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    private fun List<T>.associateWithDelegate(
        delegates: Set<AdapterDelegate<T, *>>
    ) = LinkedHashMap<Int, Pair<T, AdapterDelegate<*, *>>>().also { map ->
        forEachIndexed { index, value ->
            val delegate = requireNotNull(delegates.find { it.isFor(value as Any) })
            map[index] = value to delegate
        }
    }

}