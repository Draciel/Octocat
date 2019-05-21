package pl.draciel.octocat.core.di.base

import android.os.Bundle
import androidx.fragment.app.Fragment

abstract class BaseFragment<T> : Fragment(), ComponentProvider<T> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

}


