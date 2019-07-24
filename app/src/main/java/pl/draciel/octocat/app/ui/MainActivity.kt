package pl.draciel.octocat.app.ui

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import pl.draciel.octocat.GithubApp
import pl.draciel.octocat.R
import pl.draciel.octocat.core.di.base.BaseActivity
import pl.draciel.octocat.core.di.components.ActivityComponent
import pl.draciel.octocat.core.di.components.DaggerActivityComponent

class MainActivity : BaseActivity<ActivityComponent>() {

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
    }

    override fun buildComponent(): ActivityComponent {
        return DaggerActivityComponent.builder()
                .appComponent(GithubApp.getApplication(this).appComponent)
                .build()
    }

}
