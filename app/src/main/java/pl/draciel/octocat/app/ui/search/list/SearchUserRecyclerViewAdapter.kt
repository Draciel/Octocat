package pl.draciel.octocat.app.ui.search.list

import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.adapters.BasicRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.DelegateManager

class SearchUserRecyclerViewAdapter(
    delegateManager: DelegateManager<User, SearchUserRecyclerDelegate.ViewHolder, SearchUserRecyclerDelegate>
) :
    BasicRecyclerViewAdapter<User, SearchUserRecyclerDelegate.ViewHolder, SearchUserRecyclerDelegate>(delegateManager) {

    private var users: List<User> = emptyList()

    fun setUsers(users: List<User>) {
        this.users = users
        notifyDataSetChanged()
    }

    override fun getItem(position: Int): User = users[position]

    override fun getItemCount(): Int = users.size

    override fun onViewRecycled(holder: SearchUserRecyclerDelegate.ViewHolder) {
        holder.clear()
    }
}
