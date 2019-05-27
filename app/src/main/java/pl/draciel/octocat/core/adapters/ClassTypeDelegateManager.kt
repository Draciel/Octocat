package pl.draciel.octocat.core.adapters

import android.util.ArrayMap
import androidx.recyclerview.widget.RecyclerView

//fixme try to hide arrayMap implementation by creating builder for instance
class ClassTypeDelegateManager<T : Any, VH : RecyclerView.ViewHolder, D : RecyclerDelegate<T, VH>>(
    private val arrayMap: ArrayMap<Class<out T>, D>
) : DelegateManager<T, VH, D> {

    override fun delegateByItem(item: T): D {
        return arrayMap[item::class.java]
            ?: throw IllegalStateException("Delegate not registered for the item type ${item::class.java}")
    }

    override fun delegateByViewType(viewType: Int): D {
        return arrayMap.valueAt(viewType)
            ?: throw IllegalStateException("Delegate not registered for the view type $viewType")
    }

    override fun getViewType(item: T): Int {
        return arrayMap.indexOfKey(item::class.java)
    }
}