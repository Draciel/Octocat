package pl.draciel.octocat.app.ui.userdetails.pager.starred

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import com.bumptech.glide.Glide
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.core.adapters.ClearableViewHolder
import pl.draciel.octocat.core.adapters.RecyclerDelegate

typealias OnStarredClickListener = (starred: CodeRepository) -> Unit

class StarredRecyclerDelegate : RecyclerDelegate<CodeRepository, StarredRecyclerDelegate.ViewHolder> {

    var onStarredClickListener: OnStarredClickListener? = null

    override fun createViewHolder(parent: ViewGroup): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_code_repository, parent, false)
        return ViewHolder(itemView)
    }

    override fun bindViewHolder(viewHolder: ViewHolder, item: CodeRepository) {
        viewHolder.name.text = item.repositoryName
        viewHolder.stars.text = item.stars.toString()
        viewHolder.description.text = item.description
        viewHolder.language.text = item.language
        viewHolder.forks.text = item.forks.toString()

        //fixme extract to resources
        viewHolder.lastUpdate.text = "Updated on ${item.pushedAt}"
        viewHolder.itemView.setOnClickListener { onStarredClickListener?.invoke(item) }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        @BindView(R.id.name)
        lateinit var name: TextView

        @BindView(R.id.stars)
        lateinit var stars: TextView

        @BindView(R.id.description)
        lateinit var description: TextView

        @BindView(R.id.language)
        lateinit var language: TextView

        @BindView(R.id.forks)
        lateinit var forks: TextView

        @BindView(R.id.last_update)
        lateinit var lastUpdate: TextView

        init {
            ButterKnife.bind(this, itemView)
        }
    }

}

