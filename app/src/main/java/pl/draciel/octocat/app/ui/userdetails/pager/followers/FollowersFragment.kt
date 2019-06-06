package pl.draciel.octocat.app.ui.userdetails.pager.followers

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager
import timber.log.Timber

class FollowersFragment : PageListFragment<User>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private val followersRecyclerDelegate: FollowersRecyclerDelegate by lazy { FollowersRecyclerDelegate(imageLoader) }

    override fun onPageSelected() {
        if (loaded.compareAndSet(false, true)) {
            compositeDisposable.add(
                githubRepository.requestUserFollowers(userName)
                        .toList()
                        .subscribeOn(backgroundScheduler)
                        .observeOn(mainThreadScheduler)
                        .retry(3)
                        .subscribeBy(
                            onSuccess = { setItems(it) },
                            onError = {
                                Timber.e(it)
                                loaded.set(false)
                            }
                        )
            )
        }
    }

    override fun onStop() {
        compositeDisposable.clear()
        super.onStop()
    }

    override fun onDestroyView() {
        loaded.set(false)
        super.onDestroyView()
    }

    override fun getLayoutRes(): Int = R.layout.fragment_page_list

    override fun getPageTitle(): Int = R.string.followers

    override fun createAdapter(): PageListRecyclerViewAdapter<User, *, *> {
        return PageListRecyclerViewAdapter(SingleTypeDelegateManager(followersRecyclerDelegate))
    }

    override fun setOnItemClickListener(listener: OnItemClickListener<User>?) {
        followersRecyclerDelegate.onFollowerClickListener = listener
    }

    companion object {
        fun create(user: String): FollowersFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_NAME, user)
            return FollowersFragment().apply { arguments = bundle }
        }
    }
}
