package pl.draciel.octocat.core.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BasicRecyclerViewAdapter<T : Any, VH : RecyclerView.ViewHolder, D : RecyclerDelegate<T, VH>>(
    private val delegateManager: DelegateManager<T, VH, D>
) : RecyclerView.Adapter<VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return delegateManager.delegateByViewType(viewType).createViewHolder(parent)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = getItem(position)
        delegateManager.delegateByItem(item).bindViewHolder(holder, item)
    }

    override fun getItemViewType(position: Int): Int {
        return delegateManager.getViewType(getItem(position))
    }

    abstract fun getItem(position: Int): T

}
