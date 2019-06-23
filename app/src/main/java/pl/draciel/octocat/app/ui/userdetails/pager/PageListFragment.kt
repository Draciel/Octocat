package pl.draciel.octocat.app.ui.userdetails.pager

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.snackbar.Snackbar
import pl.draciel.octocat.R
import pl.draciel.octocat.core.mvp.ErrorView
import pl.draciel.octocat.core.mvp.ProgressView
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

typealias OnItemClickListener<T> = (item: T) -> Unit

abstract class PageListFragment<T : Any> : InjectingPageListFragment(), ProgressView, ErrorView {

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    @BindView(R.id.progress_bar)
    lateinit var progressBar: ProgressBar

    protected lateinit var adapter: PageListRecyclerViewAdapter<T, *, *>

    protected val loaded: AtomicBoolean = AtomicBoolean(false)

    private lateinit var unbinder: Unbinder

    @LayoutRes
    abstract fun getLayoutRes(): Int

    @StringRes
    abstract fun getPageTitle(): Int

    abstract fun createAdapter(): PageListRecyclerViewAdapter<T, *, *>

    abstract fun onPageSelected()

    fun setItems(items: List<T>) {
        adapter.setItems(items)
    }

    override fun showProgress() {
        progressBar.visibility = View.VISIBLE
    }

    override fun hideProgress() {
        progressBar.visibility = View.GONE
    }

    override fun showError(@StringRes message: Int) {
        Snackbar.make(view!!, message, Snackbar.LENGTH_SHORT).show()
    }

    abstract fun setOnItemClickListener(listener: OnItemClickListener<T>?)

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroyView() {
        loaded.set(false)
        recyclerView.adapter = null
        recyclerView.layoutManager = null
        unbinder.unbind()
        super.onDestroyView()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = LayoutInflater.from(context).inflate(getLayoutRes(), container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        adapter = createAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
    }
}
