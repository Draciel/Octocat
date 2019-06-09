package pl.draciel.octocat.app.ui.favourites.list

import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.favourites.list.FavouriteUserRecyclerDelegate.ViewHolder
import pl.draciel.octocat.core.adapters.BasicRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.DelegateManager

internal class FavouriteUserRecyclerViewAdapter(
    delegateManager: DelegateManager<User, ViewHolder, FavouriteUserRecyclerDelegate>
) : BasicRecyclerViewAdapter<User, ViewHolder, FavouriteUserRecyclerDelegate>(delegateManager) {

    private var users: List<User> = emptyList()

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): User = users[position]

    override fun getItemCount(): Int = users.size

    override fun onViewRecycled(holder: ViewHolder) {
        holder.clear()
    }
}
