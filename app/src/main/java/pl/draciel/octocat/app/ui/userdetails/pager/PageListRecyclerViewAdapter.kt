package pl.draciel.octocat.app.ui.userdetails.pager

import androidx.recyclerview.widget.RecyclerView
import pl.draciel.octocat.core.adapters.BasicRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.ClearableViewHolder
import pl.draciel.octocat.core.adapters.DelegateManager
import pl.draciel.octocat.core.adapters.RecyclerDelegate

class PageListRecyclerViewAdapter<T, VH, D>(delegateManager: DelegateManager<T, VH, D>) :
    BasicRecyclerViewAdapter<T, VH, D>(delegateManager)
        where T : Any,
              VH : RecyclerView.ViewHolder,
              D : RecyclerDelegate<T, VH> {

    private var items: List<T> = emptyList()

    fun setItems(users: List<T>) {
        this.items = users
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): T = items[position]

    override fun getItemCount(): Int = items.size

    override fun onViewRecycled(holder: VH) {
        if (holder is ClearableViewHolder) {
            holder.clear()
        }
    }
}
