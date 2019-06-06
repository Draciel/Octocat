package pl.draciel.octocat.app.ui.userdetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import butterknife.BindView
import butterknife.ButterKnife
import com.google.android.material.tabs.TabLayout
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
import pl.draciel.octocat.core.di.base.BaseFragment
import pl.draciel.octocat.imageloader.ImageLoader
import timber.log.Timber
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

    private lateinit var viewPagerAdapter: UserDetailsViewPagerAdapter

    private val navController: NavController by lazy { findNavController() }

    private var onCodeRepositoryClickListener: OnItemClickListener<CodeRepository>? = {
        Timber.d("Clicked repository %s", it)
    }

    private var onUserClickListener: OnItemClickListener<User>? = {
        val bundle = Bundle()
        bundle.putString(EXTRA_USER_NAME, it.login)
        navController.navigate(R.id.user_fragment, bundle)
    }

    @Inject
    lateinit var userDetailsPresenter: UserDetailsMVP.Presenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_user_details, container, false)
        ButterKnife.bind(this, view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val login = arguments?.getString(EXTRA_USER_NAME)
        userDetailsPresenter.attachView(this)
        if (login != null) {
            toolbar.title = login
            userDetailsPresenter.loadUserDetails(login)
        }
        setupViewPager()
    }

    override fun onResume() {
        super.onResume()
        if (!userDetailsPresenter.isViewAttached()) {
            userDetailsPresenter.attachView(this)
        }
    }

    override fun onStop() {
        super.onStop()
        userDetailsPresenter.detachView()
        viewPagerAdapter.pages.forEach { it.setOnItemClickListener(null) }
    }

    override fun onDestroyView() {
        viewPager.clearOnPageChangeListeners()
        viewPager.adapter = null
        imageLoader.clear(avatar)
        super.onDestroyView()
    }

    override fun onDestroy() {
        userDetailsPresenter.destroy()
        super.onDestroy()
    }

    override fun onPageScrollStateChanged(state: Int) {}

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {}

    override fun onPageSelected(position: Int) {
        viewPagerAdapter.pages[position].onPageSelected()
        val fragment: PageListFragment<*> = viewPagerAdapter.pages[position]
        attachOnClickListener(fragment)
    }

    override fun setUser(user: UserDetails) {
        imageLoader.loadImage(user.avatarUrl)
                .into(avatar)
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
        // Load first page
        onPageSelected(0)
    }

    override fun buildComponent(): UserDetailsComponent {
        return DaggerUserDetailsComponent
                .builder()
                .appComponent(GithubApp.getApplication(context!!).appComponent)
                .build()
    }

    private fun <T : Any> attachOnClickListener(fragment: PageListFragment<T>) {
        //fixme find more elegant solution
        when (fragment) {
            is CodeRepositoryFragment, is StarredFragment -> (fragment as PageListFragment<CodeRepository>).setOnItemClickListener(
                onCodeRepositoryClickListener!!
            )
            is FollowersFragment, is FollowingsFragment -> (fragment as PageListFragment<User>).setOnItemClickListener(
                onUserClickListener!!
            )
        }
    }

    private fun setupViewPager() {
        if (!this::viewPagerAdapter.isInitialized) {
            val login = arguments?.getString(EXTRA_USER_NAME) ?: ""
            this.viewPagerAdapter = UserDetailsViewPagerAdapter(
                childFragmentManager, context!!,
                listOf(
                    CodeRepositoryFragment.create(login),
                    FollowersFragment.create(login),
                    FollowingsFragment.create(login),
                    StarredFragment.create(login)
                )
            )
        }
        viewPager.adapter = viewPagerAdapter
        viewPager.addOnPageChangeListener(this)
        // We can safely retain it as they are simple, recreating them every single time is much more cpu expensive
        viewPager.offscreenPageLimit = this.viewPagerAdapter.count
        tabLayout.setupWithViewPager(viewPager)
    }
}

private fun TextView.setTextAndShow(text: String?) {
    if (!text.isNullOrBlank()) {
        this.text = text
        this.visibility = View.VISIBLE
    }
}
