package pl.draciel.octocat.app.ui.userdetails.pager.starred

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager

class StarredFragment : PageListFragment<CodeRepository>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private var starredRepositoryRecyclerDelegate: StarredRecyclerDelegate = StarredRecyclerDelegate()

    override fun onPageSelected() {
        if (loaded.compareAndSet(false, true)) {
            showProgress()
            compositeDisposable.add(
                githubRepository.requestUserStarredCodeRepositories(userName)
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
