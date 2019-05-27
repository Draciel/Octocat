package pl.draciel.octocat.core.adapters

import android.util.ArrayMap
import androidx.recyclerview.widget.RecyclerView

class MultiClassTypeDelegateManager<T : Any, VH : RecyclerView.ViewHolder, D : ComplexRecyclerDelegate<T, VH>>(
    private val arrayMap: ArrayMap<Class<out T>, List<D>>
) : DelegateManager<T, VH, D> {

    private val sublistMaxSize: Int = arrayMap.values.maxBy { it.size }?.size ?: 0

    override fun delegateByItem(item: T): D {
        val delegates = arrayMap[item::class.java]
        if (delegates.isNullOrEmpty()) {
            throw IllegalStateException("Delegate not registered for the item type ${item::class.java}")
        }
        return delegates.firstOrNull { it.test(item) }
            ?: throw IllegalStateException("Delegate not found for item type ${item::class.java}")
    }

    override fun delegateByViewType(viewType: Int): D {
        val r = viewType % sublistMaxSize
        val i = viewType / sublistMaxSize

        return arrayMap.valueAt(i)[r]
            ?: throw IllegalStateException("Delegate not registered for the view type $viewType")
    }

    override fun getViewType(item: T): Int {
        val i = arrayMap.indexOfKey(item::class.java)
        val r = arrayMap.valueAt(i).indexOfFirst { it.test(item) }
        return i * sublistMaxSize + r
    }
}
