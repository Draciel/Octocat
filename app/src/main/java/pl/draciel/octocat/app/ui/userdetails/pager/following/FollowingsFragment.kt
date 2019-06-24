package pl.draciel.octocat.app.ui.userdetails.pager.following

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager

class FollowingsFragment : PageListFragment<User>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private val followingRecyclerDelegate: FollowingRecyclerDelegate by lazy { FollowingRecyclerDelegate(imageLoader) }

    override fun onResume() {
        super.onResume()
        if (loaded.compareAndSet(false, true)) {
            showProgress()
            compositeDisposable.add(
                githubRepository.requestUserFollowing(userName)
                        .toList()
                        .retry(3)
                        .subscribeOn(backgroundScheduler)
                        .observeOn(mainThreadScheduler)
                        .doOnEvent { _, _ -> hideProgress() }
                        .doOnDispose { hideProgress() }
                        .subscribeBy(
                            onSuccess = { setItems(it) },
                            onError = {
                                // fixme enhance errors text based on http status/no internet
                                showError(R.string.something_went_wrong)
                                loaded.set(false)
                            }
                        )
            )
        }
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
