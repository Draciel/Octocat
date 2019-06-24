package pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory

import android.os.Bundle
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager

class CodeRepositoryFragment : PageListFragment<CodeRepository>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private var codeRepositoryRecyclerDelegate: CodeRepositoryRecyclerDelegate = CodeRepositoryRecyclerDelegate()

    override fun onResume() {
        super.onResume()
        if (loaded.compareAndSet(false, true)) {
            showProgress()
            compositeDisposable.add(
                githubRepository.requestUserCodeRepositories(userName)
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

    override fun getPageTitle(): Int = R.string.code_repository

    override fun createAdapter(): PageListRecyclerViewAdapter<CodeRepository, *, *> {
        return PageListRecyclerViewAdapter(SingleTypeDelegateManager(codeRepositoryRecyclerDelegate))
    }

    override fun setOnItemClickListener(listener: OnItemClickListener<CodeRepository>?) {
        codeRepositoryRecyclerDelegate.onCodeRepositoryClickListener = listener
    }

    companion object {
        fun create(user: String): CodeRepositoryFragment {
            val bundle = Bundle()
            bundle.putString(EXTRA_USER_NAME, user)
            return CodeRepositoryFragment().apply { arguments = bundle }
        }
    }
}
