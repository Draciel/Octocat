package pl.draciel.octocat.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.concurrent.SchedulerSupportExtension
import pl.draciel.octocat.core.di.base.BaseFragment
import javax.inject.Inject

internal class SearchFragment : BaseFragment<SearchComponent>(), SearchMVP.View {

    @Inject
    lateinit var searchPresenter: SearchMVP.Presenter

    @Inject
    @MainThreadScheduler
    lateinit var uiScheduler: Scheduler

    @Inject
    @ComputationScheduler
    lateinit var computationScheduler: Scheduler

    @BindView(R.id.search)
    lateinit var searchView: SearchView

    private var rxSearchQueryListener: RxSearchQueryListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_search, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupportExtension.MAIN_THREAD_SCHEDULER)
    override fun observeOnSearchChanged(): Observable<String> {
        if (rxSearchQueryListener == null) {
            rxSearchQueryListener = RxSearchQueryListener()
            searchView.setOnQueryTextListener(rxSearchQueryListener)
        }
        return rxSearchQueryListener!!.observeOnTextChanged()
                .doOnDispose { disposeRxSearchQueryListener() }
    }

    @CheckReturnValue
    @SchedulerSupport(SchedulerSupportExtension.MAIN_THREAD_SCHEDULER)
    override fun observeOnSearchSubmit(): Observable<String> {
        if (rxSearchQueryListener == null) {
            rxSearchQueryListener = RxSearchQueryListener()
            searchView.setOnQueryTextListener(rxSearchQueryListener)
        }
        return rxSearchQueryListener!!.observeOnTextSubmit()
                .doOnDispose { disposeRxSearchQueryListener() }
    }

    private fun disposeRxSearchQueryListener() {
        if (rxSearchQueryListener != null) {
            searchView.setOnQueryTextListener(null)
            rxSearchQueryListener = null
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchPresenter.attachView(this)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    override fun onDestroy() {
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
