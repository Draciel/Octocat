package pl.draciel.octocat.app.ui.favourites.list

import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.ui.favourites.list.FavouriteUserRecyclerDelegate.ViewHolder
import pl.draciel.rad.BasicRecyclerViewAdapter
import pl.draciel.rad.DelegateManager

internal class FavouriteUserRecyclerViewAdapter(
    delegateManager: DelegateManager<FavouriteUser, ViewHolder, FavouriteUserRecyclerDelegate>
) : BasicRecyclerViewAdapter<FavouriteUser, ViewHolder, FavouriteUserRecyclerDelegate>(delegateManager) {

    private var users: List<FavouriteUser> = emptyList()

    fun setUsers(users: List<FavouriteUser>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): FavouriteUser = users[position]

    override fun getItemCount(): Int = users.size

    override fun onViewRecycled(holder: ViewHolder) {
        holder.clear()
    }
}
