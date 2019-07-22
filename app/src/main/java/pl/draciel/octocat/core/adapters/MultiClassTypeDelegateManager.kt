package pl.draciel.octocat.core.adapters

import android.util.ArrayMap
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class MultiClassTypeDelegateManager<T : Any, VH : RecyclerView.ViewHolder, D : ComplexRecyclerDelegate<T, VH>>
private constructor(private val arrayMap: ArrayMap<Class<out T>, List<D>>) : DelegateManager<T, VH, D> {

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

    class Builder<T : Any, VH : RecyclerView.ViewHolder, D : ComplexRecyclerDelegate<T, VH>>(capacity: Int? = null) {
        private val map: ArrayMap<Class<out T>, MutableList<D>> = capacity?.let { ArrayMap(it) } ?: ArrayMap()
        private var isBuilt = false

        fun register(type: Class<out T>, vararg delegates: D): Builder<T, VH, D> {
            if (!isBuilt) {
                var list = map[type]
                if (list == null) {
                    list = mutableListOf(*delegates)
                    map[type] = list
                }
                list.addAll(delegates)
            }
            return this
        }

        fun register(type: Class<out T>, delegates: List<D>): Builder<T, VH, D> {
            if (!isBuilt) {
                var list = map[type]
                if (list == null) {
                    list = mutableListOf()
                    map[type] = list
                }
                list.addAll(delegates)
            }
            return this
        }

        fun register(type: Class<out T>, delegate: D): Builder<T, VH, D> {
            if (!isBuilt) {
                var list = map[type]
                if (list == null) {
                    list = mutableListOf()
                    map[type] = list
                }
                list.add(delegate)
            }
            return this
        }

        fun build(): MultiClassTypeDelegateManager<T, VH, D> {
            isBuilt = true
            val copy = ArrayMap<Class<out T>, List<D>>(map.size)
            map.forEach { copy[it.key] = Collections.unmodifiableList(it.value) }
            return MultiClassTypeDelegateManager(copy)
        }
    }
}
