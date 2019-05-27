package pl.draciel.octocat.app.ui.search.list

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.adapters.ClearableViewHolder
import pl.draciel.octocat.core.adapters.RecyclerDelegate

typealias OnUserClickListener = (user: User) -> Unit

class SearchUserRecyclerDelegate : RecyclerDelegate<User, SearchUserRecyclerDelegate.ViewHolder> {

    var onUserClickListener: OnUserClickListener? = null

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_user, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(viewHolder: ViewHolder, item: User) {
        viewHolder.login.text = item.login
        viewHolder.itemView.setOnClickListener { onUserClickListener?.invoke(item) }

        //fixme refactor it later
        Glide.with(viewHolder.itemView)
                .load(item.avatarUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .fitCenter()
                .into(viewHolder.avatar)
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), ClearableViewHolder {

        @BindView(R.id.login)
        lateinit var login: TextView

        @BindView(R.id.avatar)
        lateinit var avatar: ImageView

        init {
            ButterKnife.bind(this, itemView)
        }

        override fun clear() {
            Glide.with(itemView).clear(avatar)
        }
    }

}
