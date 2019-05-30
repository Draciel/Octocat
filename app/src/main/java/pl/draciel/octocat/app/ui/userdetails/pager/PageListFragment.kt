package pl.draciel.octocat.app.ui.userdetails.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.core.di.base.BaseFragment
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

typealias OnItemClickListener<T> = (item: T) -> Unit

abstract class PageListFragment<T : Any> : InjectingPageListFragment() {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    protected lateinit var adapter: PageListRecyclerViewAdapter<T, *, *>

    protected val loaded: AtomicBoolean = AtomicBoolean(false)

    @LayoutRes
    abstract fun getLayoutRes(): Int

    @StringRes
    abstract fun getPageTitle(): Int

    abstract fun createAdapter(): PageListRecyclerViewAdapter<T, *, *>

    abstract fun onPageSelected()

    fun setItems(items: List<T>) {
        adapter.setItems(items)
    }

    abstract fun setOnItemClickListener(listener: OnItemClickListener<T>?)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(getLayoutRes(), container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(context)
    }
}
