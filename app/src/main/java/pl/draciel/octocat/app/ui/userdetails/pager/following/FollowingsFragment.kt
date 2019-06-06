package pl.draciel.octocat.app.ui.userdetails.pager.following

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory.CodeRepositoryFragment
import pl.draciel.octocat.app.ui.userdetails.pager.followers.FollowersRecyclerDelegate
import pl.draciel.octocat.app.ui.userdetails.pager.following.FollowingRecyclerDelegate
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager
import timber.log.Timber

class FollowingsFragment : PageListFragment<User>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private val followingRecyclerDelegate: FollowingRecyclerDelegate by lazy { FollowingRecyclerDelegate(imageLoader) }

    override fun onPageSelected() {
        if (loaded.compareAndSet(false, true)) {
            compositeDisposable.add(
                githubRepository.requestUserFollowing(userName)
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

    override fun getPageTitle(): Int = R.string.following

    override fun createAdapter(): PageListRecyclerViewAdapter<User, *, *> {
        return PageListRecyclerViewAdapter(SingleTypeDelegateManager(followingRecyclerDelegate))
    }

    override fun setOnItemClickListener(listener: OnItemClickListener<User>?) {
        followingRecyclerDelegate.onFollowingClickListener = listener
    }

    companion object {
        fun create(user: String): FollowingsFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_NAME, user)
            return FollowingsFragment().apply { arguments = bundle }
        }
    }
}
