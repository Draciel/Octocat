package pl.draciel.octocat.app.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.NavGraphDirections
import pl.draciel.octocat.R
import pl.draciel.octocat.core.di.base.BaseActivity
import pl.draciel.octocat.core.di.components.ActivityComponent
import pl.draciel.octocat.core.di.components.DaggerActivityComponent

class MainActivity : BaseActivity<ActivityComponent>() {

    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        val currentDestination = navController.currentDestination?.id ?: 0
        when (item.itemId) {
            R.id.navigation_search -> {
                if (currentDestination != R.id.search_fragment) {
                    navController.navigate(NavGraphDirections.actionGlobalSearchFragment())
                }
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_favourites -> {
                if (currentDestination != R.id.favourites_fragment) {
                    navController.navigate(NavGraphDirections.actionGlobalFavouritesFragment())
                }
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
    }

    override fun buildComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(GithubApp.getApplication(this).appComponent)
                .build()
    }

}
