package pl.draciel.octocat.app.ui.userdetails.pager.coderepostiory

import android.os.Bundle
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.CodeRepository
import pl.draciel.octocat.app.ui.userdetails.EXTRA_USER_NAME
import pl.draciel.octocat.app.ui.userdetails.pager.OnItemClickListener
import pl.draciel.octocat.app.ui.userdetails.pager.PageListComponent
import pl.draciel.octocat.app.ui.userdetails.pager.PageListFragment
import pl.draciel.octocat.app.ui.userdetails.pager.PageListRecyclerViewAdapter
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.adapters.SingleTypeDelegateManager
import pl.draciel.octocat.github.GithubRepository
import timber.log.Timber
import javax.inject.Inject

class CodeRepositoryFragment : PageListFragment<CodeRepository>() {

    private val userName: String by lazy { arguments?.getString(EXTRA_USER_NAME) ?: "" }

    private var codeRepositoryRecyclerDelegate: CodeRepositoryRecyclerDelegate = CodeRepositoryRecyclerDelegate()

    override fun onPageSelected() {
        if (loaded.compareAndSet(false, true)) {
            compositeDisposable.add(
                githubRepository.requestUserCodeRepositories(userName)
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
