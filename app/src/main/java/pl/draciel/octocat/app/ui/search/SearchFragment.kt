package pl.draciel.octocat.app.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import io.reactivex.Observable
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.annotations.SchedulerSupport
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.search.list.OnUserClickListener
import pl.draciel.octocat.app.ui.search.list.SearchUserRecyclerDelegate
import pl.draciel.octocat.app.ui.search.list.SearchUserRecyclerViewAdapter
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.concurrent.SchedulerSupportExtension
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager
import pl.draciel.octocat.core.di.base.BaseFragment
import pl.draciel.octocat.imageloader.ImageLoader
import javax.inject.Inject

internal class SearchFragment : BaseFragment<SearchComponent>(), SearchMVP.View {

    @Inject
    lateinit var searchPresenter: SearchMVP.Presenter

    @BindView(R.id.search)
    lateinit var searchView: SearchView

    @BindView(R.id.search_recycler_view)
    lateinit var searchResultRecyclerView: RecyclerView

    @Inject
    lateinit var imageLoader: ImageLoader

    private val onUserClickListener: OnUserClickListener = {
        val bundle = Bundle()
        bundle.putString(EXTRA_USER_NAME, it.login)
        navController.navigate(R.id.user_fragment, bundle)
    }

    private lateinit var searchAdapter: SearchUserRecyclerViewAdapter
    private lateinit var searchUserRecyclerDelegate: SearchUserRecyclerDelegate

    private var rxSearchQueryListener: RxSearchQueryListener? = null

    private val navController: NavController by lazy { findNavController() }

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
        if (!this::searchAdapter.isInitialized) {
            searchUserRecyclerDelegate = SearchUserRecyclerDelegate(imageLoader)
            searchAdapter = SearchUserRecyclerViewAdapter(SingleTypeDelegateManager(searchUserRecyclerDelegate))
        }
        searchUserRecyclerDelegate.onUserClickListener = onUserClickListener
        searchResultRecyclerView.adapter = searchAdapter
        searchResultRecyclerView.layoutManager = LinearLayoutManager(context)
        searchPresenter.attachView(this)
    }

    override fun updateResults(users: List<User>) {
        searchAdapter.setUsers(users)
    }

    override fun onStop() {
        super.onStop()
        searchPresenter.detachView()
        searchUserRecyclerDelegate.onUserClickListener = null
    }

    override fun onResume() {
        super.onResume()
        if (!searchPresenter.isViewAttached()) {
            searchUserRecyclerDelegate.onUserClickListener = onUserClickListener
            searchPresenter.attachView(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        searchPresenter.destroy()
        searchResultRecyclerView.adapter = null
        searchResultRecyclerView.layoutManager = null
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
