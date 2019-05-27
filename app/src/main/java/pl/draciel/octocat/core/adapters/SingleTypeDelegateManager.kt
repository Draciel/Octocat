package pl.draciel.octocat.core.adapters

import androidx.recyclerview.widget.RecyclerView

class SingleTypeDelegateManager<T : Any, VH : RecyclerView.ViewHolder, D : RecyclerDelegate<T, VH>>(
    private val recyclerDelegate: D
) : DelegateManager<T, VH, D> {

    override fun delegateByItem(item: T): D {
        return recyclerDelegate
    }

    override fun delegateByViewType(viewType: Int): D {
        return recyclerDelegate
    }

    override fun getViewType(item: T): Int {
        return 0
    }
}
