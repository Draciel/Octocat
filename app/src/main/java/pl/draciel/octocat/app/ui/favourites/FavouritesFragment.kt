package pl.draciel.octocat.app.ui.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.FavouriteUser
import pl.draciel.octocat.app.ui.favourites.list.FavouriteUserRecyclerDelegate
import pl.draciel.octocat.app.ui.favourites.list.FavouriteUserRecyclerViewAdapter
import pl.draciel.octocat.app.ui.favourites.list.OnUserClickListener
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.core.di.base.BaseFragment
import pl.draciel.octocat.imageloader.ImageLoader
import pl.draciel.rad.SingleTypeDelegateManager
import javax.inject.Inject

class FavouritesFragment : BaseFragment<FavouritesComponent>(), FavouritesMVP.View {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.recycler_view)
    lateinit var recyclerView: RecyclerView

    @Inject
    lateinit var presenter: FavouritesMVP.Presenter

    @Inject
    lateinit var imageLoader: ImageLoader

    private val navController: NavController by lazy { findNavController() }

    private lateinit var unbinder: Unbinder

    private val onUserClickListener: OnUserClickListener = {
        val bundle = Bundle()
        bundle.putString(EXTRA_USER_NAME, it.login)
        navController.navigate(R.id.user_fragment, bundle)
    }

    private lateinit var delegate: FavouriteUserRecyclerDelegate
    private lateinit var adapter: FavouriteUserRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_favourites, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (!this::delegate.isInitialized) {
            delegate = FavouriteUserRecyclerDelegate(imageLoader)
            adapter = FavouriteUserRecyclerViewAdapter(SingleTypeDelegateManager(delegate))
        }
        toolbar.title = "Favourites"
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(activity)
        delegate.onUserClickListener = onUserClickListener
        presenter.attachView(this)
        presenter.loadFavouriteUsers()
    }

    override fun setUsers(users: List<FavouriteUser>) {
        adapter.setUsers(users)
    }

    override fun onResume() {
        super.onResume()
        if (!presenter.isViewAttached()) {
            presenter.attachView(this)
            delegate.onUserClickListener = onUserClickListener
        }
    }

    override fun onStop() {
        super.onStop()
        presenter.detachView()
        delegate.onUserClickListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        recyclerView.adapter = null
        recyclerView.layoutManager = null
        unbinder.unbind()
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.destroy()
    }

    override fun buildComponent(): FavouritesComponent {
        return DaggerFavouritesComponent.builder()
                .appComponent(GithubApp.getApplication(context!!).appComponent)
                .build()
    }
}
