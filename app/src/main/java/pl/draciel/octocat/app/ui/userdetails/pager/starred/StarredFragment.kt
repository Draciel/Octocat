package pl.draciel.octocat.app.ui.userdetails.pager.starred

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory.CodeRepositoryFragment
import pl.draciel.octocat.app.ui.userdetails.pager.following.FollowingRecyclerDelegate
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager
import timber.log.Timber

class StarredFragment : PageListFragment<CodeRepository>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private var starredRepositoryRecyclerDelegate: StarredRecyclerDelegate = StarredRecyclerDelegate()

    // todo investigate if this is needed
    override fun onPageSelected() {
        if (loaded.compareAndSet(false, true)) {
            compositeDisposable.add(
                githubRepository.requestUserStarredCodeRepositories(userName)
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

    override fun getPageTitle(): Int = R.string.starred

    override fun createAdapter(): PageListRecyclerViewAdapter<CodeRepository, *, *> {
        return PageListRecyclerViewAdapter(SingleTypeDelegateManager(starredRepositoryRecyclerDelegate))
    }

    override fun setOnItemClickListener(listener: OnItemClickListener<CodeRepository>?) {
        starredRepositoryRecyclerDelegate.onStarredClickListener = listener
    }

    companion object {
        fun create(user: String): StarredFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_NAME, user)
            return StarredFragment().apply { arguments = bundle }
        }
    }
}
