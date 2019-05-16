package pl.draciel.octocat.app.ui

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.navigation.NavController
import androidx.navigation.findNavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.di.components.ActivityComponent
import pl.draciel.octocat.core.di.components.DaggerActivityComponent
import pl.draciel.octocat.github.GithubRepository
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var githubRepository: GithubRepository

    private val compositeDisposable = CompositeDisposable()

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_search -> {
                navController.navigate(R.id.search_fragment)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourites -> {
                navController.navigate(R.id.favourites_fragment)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        buildComponent().inject(this)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        compositeDisposable.add(
            githubRepository.requestUser("Draciel")
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<User>() {
                        override fun onSuccess(t: User) {
                            Timber.d("User %s", t)
                        }

                        override fun onError(e: Throwable) {
                            Timber.e(e)
                        }
                    })
        )
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }

    fun buildComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(GithubApp.getApplication(this).appComponent)
                .build()
    }

}
