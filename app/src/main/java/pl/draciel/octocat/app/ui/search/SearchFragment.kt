package pl.draciel.octocat.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.UiScheduler
import pl.draciel.octocat.core.di.base.BaseFragment
import javax.inject.Inject

internal class SearchFragment : BaseFragment<SearchComponent>(), SearchMVP.View {

    @Inject
    lateinit var searchPresenter: SearchMVP.Presenter

    @Inject
    @UiScheduler
    lateinit var uiScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildComponent().inject(this)
        searchPresenter.attachView(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        toolbar.title = "Search"
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    // At the time we build component, context will be ready
    override fun buildComponent(): SearchComponent {
        return DaggerSearchComponent.builder()
                .appComponent(GithubApp.getApplication(context!!).appComponent)
                .build()
    }

    companion object {
        fun create() = SearchFragment()
    }
}
