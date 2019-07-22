package pl.draciel.octocat.app.ui.favourites.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.ui.favourites.list.FavouriteUserRecyclerDelegate.ViewHolder
import pl.draciel.octocat.core.adapters.ClearableViewHolder
import pl.draciel.octocat.imageloader.ImageLoader
import pl.draciel.rad.RecyclerDelegate

typealias OnUserClickListener = (user: FavouriteUser) -> Unit

internal class FavouriteUserRecyclerDelegate(private val imageLoader: ImageLoader) :
    RecyclerDelegate<FavouriteUser, ViewHolder> {

    var onUserClickListener: OnUserClickListener? = null

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_favourite_user, parent, false)
        return ViewHolder(itemView, imageLoader)
    }

    override fun bindViewHolder(viewHolder: ViewHolder, item: FavouriteUser) {
        viewHolder.login.text = item.login
        if (item.bio == null) {
            viewHolder.bio.setText(R.string.no_bio)
        } else {
            viewHolder.bio.text = item.bio
        }
        viewHolder.company.updateTextVisibility(item.company)
        viewHolder.itemView.setOnClickListener { onUserClickListener?.invoke(item) }

        imageLoader.loadImage(item.avatarUrl)
                .into(viewHolder.avatar)
    }

    class ViewHolder(itemView: View, private val imageLoader: ImageLoader) : RecyclerView.ViewHolder(itemView),
        ClearableViewHolder {

        @BindView(R.id.login)
        lateinit var login: TextView

        @BindView(R.id.avatar)
        lateinit var avatar: ImageView

        @BindView(R.id.bio)
        lateinit var bio: TextView

        @BindView(R.id.company)
        lateinit var company: TextView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun clear() {
            imageLoader.clear(avatar)
        }
    }
}

private fun TextView.updateTextVisibility(text: String?) {
    if (!text.isNullOrBlank()) {
        this.text = text
        this.visibility = View.VISIBLE
    } else {
        this.text = ""
        this.visibility = View.GONE
    }
}
