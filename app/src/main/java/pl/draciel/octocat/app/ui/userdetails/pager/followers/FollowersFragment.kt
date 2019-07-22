package pl.draciel.octocat.app.ui.userdetails.pager.followers

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.rad.SingleTypeDelegateManager

class FollowersFragment : PageListFragment<User>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private val followersRecyclerDelegate: FollowersRecyclerDelegate by lazy { FollowersRecyclerDelegate(imageLoader) }

    override fun onResume() {
        super.onResume()
        if (loaded.compareAndSet(false, true)) {
            showProgress()
            compositeDisposable.add(
                githubRepository.requestUserFollowers(userName)
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
