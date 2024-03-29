package pl.draciel.octocat.app.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.tabs.TabLayout
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.annotations.CheckReturnValue
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposables
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.model.UserDetails
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.UserDetailsViewPagerAdapter
import pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory.CodeRepositoryFragment
import pl.draciel.octocat.app.ui.userdetails.pager.followers.FollowersFragment
import pl.draciel.octocat.app.ui.userdetails.pager.following.FollowingsFragment
import pl.draciel.octocat.app.ui.userdetails.pager.starred.StarredFragment
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.base.BaseFragment
import pl.draciel.octocat.core.utility.NavigationUtility
import pl.draciel.octocat.imageloader.ImageLoader
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val EXTRA_USER_NAME = "UserDetailsFragment::UserName"

class UserDetailsFragment : BaseFragment<UserDetailsComponent>(), UserDetailsMVP.View, ViewPager.OnPageChangeListener {

    @BindView(R.id.toolbar)
    lateinit var toolbar: Toolbar

    @BindView(R.id.avatar)
    lateinit var avatar: ImageView

    @BindView(R.id.name)
    lateinit var nameTextView: TextView

    @BindView(R.id.company)
    lateinit var companyTextView: TextView

    @BindView(R.id.location)
    lateinit var locationTextView: TextView

    @BindView(R.id.email)
    lateinit var emailTextView: TextView

    @BindView(R.id.blog)
    lateinit var blogTextView: TextView

    @BindView(R.id.bio)
    lateinit var bioTextView: TextView

    @BindView(R.id.view_pager)
    lateinit var viewPager: ViewPager

    @BindView(R.id.tab_layout)
    lateinit var tabLayout: TabLayout

    @Inject
    lateinit var imageLoader: ImageLoader

    @Inject
    @MainThreadScheduler
    lateinit var uiThreadScheduler: Scheduler

    @Inject
    lateinit var userDetailsPresenter: UserDetailsMVP.Presenter

    private lateinit var viewPagerAdapter: UserDetailsViewPagerAdapter

    private val navController: NavController by lazy { findNavController() }

    private var currentUser: UserDetails? = null

    private lateinit var unbinder: Unbinder

    private var onCodeRepositoryClickListener: OnItemClickListener<CodeRepository>? = {}
    private var onEmailClickListener: View.OnClickListener? =
        View.OnClickListener { NavigationUtility.openMailClient(emailTextView.text.toString(), context!!) }
    private var onBlogClickListener: View.OnClickListener? =
        View.OnClickListener { NavigationUtility.openWebBrowser(blogTextView.text.toString(), context!!) }
    private val args: UserDetailsFragmentArgs by navArgs()

    private var onUserClickListener: OnItemClickListener<User>? = {
        navController.navigate(UserDetailsFragmentDirections.actionUserFragmentToUserFragment(it.login))
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)
        unbinder = ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val login = args.userName
        userDetailsPresenter.attachView(this)
        toolbar.title = login
        userDetailsPresenter.loadUserDetails(login)
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()
        if (!userDetailsPresenter.isViewAttached()) {
            userDetailsPresenter.attachView(this)
        }
        observeOnMenuItemClick()
        blogTextView.setOnClickListener(onBlogClickListener)
        emailTextView.setOnClickListener(onEmailClickListener)
    }

    override fun onStop() {
        super.onStop()
        userDetailsPresenter.detachView()
        compositeDisposable.clear()
        blogTextView.setOnClickListener(null)
        emailTextView.setOnClickListener(null)
    }

    override fun onDestroyView() {
        viewPager.clearOnPageChangeListeners()
        viewPager.adapter = null
        imageLoader.clear(avatar)
        compositeDisposable.clear()
        unbinder.unbind()
        super.onDestroyView()
    }

    override fun onDestroy() {
        userDetailsPresenter.destroy()
        super.onDestroy()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        val fragment: PageListFragment<*> = viewPagerAdapter.getItem(position)
        attachOnClickListener(fragment)
    }

    override fun setUserDetails(user: UserDetails) {
        currentUser = user
        imageLoader.loadImage(user.avatarUrl).into(avatar)
        nameTextView.setTextAndShow(user.name)
        companyTextView.setTextAndShow(user.company)
        locationTextView.setTextAndShow(user.location)
        emailTextView.setTextAndShow(user.email)
        blogTextView.setTextAndShow(user.blog)
        if (user.bio.isNullOrBlank()) {
            bioTextView.text = getString(R.string.no_bio)
        } else {
            bioTextView.text = user.bio
        }
        blogTextView.setOnClickListener(onBlogClickListener)
        emailTextView.setOnClickListener(onEmailClickListener)
        // Load first page
        onPageSelected(0)
    }

    override fun setFavouriteChecked(checked: Boolean) {
        setupToolbar()
        toolbar.menu.findItem(R.id.add_to_favourites).updateCheckedState(checked)
    }

    override fun buildComponent(): UserDetailsComponent {
        return DaggerUserDetailsComponent
                .builder()
                .appComponent(GithubApp.getApplication(context!!).appComponent)
                .build()
    }

    private fun <T : Any> attachOnClickListener(fragment: PageListFragment<T>) {
        when (fragment) {
            is CodeRepositoryFragment -> fragment.setOnItemClickListener(onCodeRepositoryClickListener)
            is StarredFragment -> fragment.setOnItemClickListener(onCodeRepositoryClickListener)
            is FollowersFragment -> fragment.setOnItemClickListener(onUserClickListener)
            is FollowingsFragment -> fragment.setOnItemClickListener(onUserClickListener)
        }
    }

    private fun setupToolbar() {
        toolbar.inflateMenu(R.menu.user_details_toolbar_menu)
        observeOnMenuItemClick()
    }

    private fun setupViewPager() {
        if (!this::viewPagerAdapter.isInitialized) {
            val login = args.userName
            this.viewPagerAdapter = UserDetailsViewPagerAdapter(
                childFragmentManager, context!!,
                listOf(
                    { CodeRepositoryFragment.create(login) },
                    { FollowersFragment.create(login) },
                    { FollowingsFragment.create(login) },
                    { StarredFragment.create(login) }
                ))
        }
        viewPager.adapter = viewPagerAdapter
        // We can safely retain it as they are simple, recreating them every single time is much more cpu expensive
        viewPager.offscreenPageLimit = this.viewPagerAdapter.count
        tabLayout.setupWithViewPager(viewPager)
        viewPager.addOnPageChangeListener(this)
    }

    private fun observeOnMenuItemClick() {
        compositeDisposable.add(toolbar.observeOnMenuItemClick()
                .doOnNext {
                    when (it.itemId) {
                        R.id.add_to_favourites -> {
                            it.updateCheckedState(!it.isChecked)
                        }
                    }
                }
                .debounce(200, TimeUnit.MILLISECONDS)
                .observeOn(uiThreadScheduler)
                .subscribeBy(
                    onNext = {
                        when (it.itemId) {
                            R.id.add_to_favourites -> {
                                if (it.isChecked) {
                                    userDetailsPresenter.saveUserInFavourites(currentUser!!)
                                } else {
                                    userDetailsPresenter.removeUserFromFavourites(currentUser!!)
                                }
                            }
                            else -> throw IllegalArgumentException("Did you forget to register new menu id?")
                        }
                    },
                    onError = { Timber.e(it) },
                    onComplete = {}
                ))
    }
}

private fun TextView.setTextAndShow(text: String?) {
    if (!text.isNullOrBlank()) {
        this.text = text
        this.visibility = View.VISIBLE
    }
}

private fun MenuItem.updateCheckedState(checked: Boolean) {
    isChecked = checked
    if (checked) {
        setIcon(R.drawable.ic_favourite_24px)
    } else {
        setIcon(R.drawable.ic_favourite_border_24px)
    }
}

@CheckReturnValue
private fun Toolbar.observeOnMenuItemClick(): Observable<MenuItem> {
    return Observable.create { emitter ->
        val listener = Toolbar.OnMenuItemClickListener {
            try {
                emitter.onNext(it)
            } catch (npe: NullPointerException) {
                throw npe
            } catch (ex: Exception) {
                emitter.onError(ex)
            }
            false
        }
        setOnMenuItemClickListener(listener)
        emitter.setDisposable(Disposables.fromAction { setOnMenuItemClickListener(null) })
    }
}
