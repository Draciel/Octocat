package pl.draciel.octocat.app.ui.userdetails.pager

import android.os.Bundle
import io.reactivex.Scheduler
import io.reactivex.disposables.CompositeDisposable
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.concurrent.ComputationScheduler
import pl.draciel.octocat.concurrent.MainThreadScheduler
import pl.draciel.octocat.core.di.base.BaseFragment
import pl.draciel.octocat.github.GithubRepository
import javax.inject.Inject

/**
 * This class injects base dependencies used in [PageListFragment]. This class helps avoiding copy/pasting
 * common code across [PageListFragment]. Injection can't be done in [PageListFragment]
 * because Dagger2 doesn't accept injection to generic classes so it must be in separated classes.
 */
abstract class InjectingPageListFragment : BaseFragment<PageListComponent>() {

    @Inject
    protected lateinit var githubRepository: GithubRepository

    @Inject
    @ComputationScheduler
    protected lateinit var backgroundScheduler: Scheduler

    @Inject
    @MainThreadScheduler
    protected lateinit var mainThreadScheduler: Scheduler

    protected val compositeDisposable: CompositeDisposable = CompositeDisposable()

    private var component: PageListComponent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (component == null) {
            component = buildComponent()
        }
        component!!.inject(this)
    }

    override fun onDestroy() {
        component = null
        super.onDestroy()
    }

    override fun buildComponent(): PageListComponent {
        return DaggerPageListComponent
                .builder()
                .appComponent(GithubApp.getApplication(context!!).appComponent)
                .build()
    }
}
