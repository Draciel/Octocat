package pl.draciel.octocat

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.core.app.ActivityCompat
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import pl.draciel.octocat.app.model.User
import pl.draciel.octocat.core.di.components.ActivityComponent
import pl.draciel.octocat.core.di.components.DaggerActivityComponent
import pl.draciel.octocat.github.api.GithubRepository
import timber.log.Timber
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var githubRepository: GithubRepository

    private val compositeDisposable = CompositeDisposable()

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_home -> {
                textMessage.setText(R.string.title_home)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_dashboard -> {
                textMessage.setText(R.string.title_dashboard)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_notifications -> {
                textMessage.setText(R.string.title_notifications)
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

        textMessage = findViewById(R.id.message)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        compositeDisposable.add(
            githubRepository.getUser("draciel")
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
