package pl.draciel.octocat.core.adapters

import androidx.recyclerview.widget.RecyclerView

interface DelegateManager<T : Any, VH : RecyclerView.ViewHolder, D : RecyclerDelegate<T, VH>> {

    fun delegateByItem(item: T): D

    fun delegateByViewType(viewType: Int): D

    fun getViewType(item: T): Int

}
