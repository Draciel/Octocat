package pl.draciel.octocat.app.ui.userdetails.pager.followers

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.userdetails.pager.followers.FollowersRecyclerDelegate.ViewHolder
import pl.draciel.octocat.core.adapters.ClearableViewHolder
import pl.draciel.octocat.imageloader.ImageLoader
import pl.draciel.rad.RecyclerDelegate

typealias OnFollowerClickListener = (user: User) -> Unit

class FollowersRecyclerDelegate(private val imageLoader: ImageLoader) : RecyclerDelegate<User, ViewHolder> {

    var onFollowerClickListener: OnFollowerClickListener? = null

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView, imageLoader)
    }

    override fun bindViewHolder(viewHolder: ViewHolder, item: User) {
        viewHolder.login.text = item.login
        viewHolder.itemView.setOnClickListener { onFollowerClickListener?.invoke(item) }

        imageLoader.loadImage(item.avatarUrl)
                .into(viewHolder.avatar)
    }

    class ViewHolder(itemView: View, private val imageLoader: ImageLoader) : RecyclerView.ViewHolder(itemView),
        ClearableViewHolder {

        @BindView(R.id.login)
        lateinit var login: TextView

        @BindView(R.id.avatar)
        lateinit var avatar: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun clear() {
            imageLoader.clear(avatar)
        }
    }

}
