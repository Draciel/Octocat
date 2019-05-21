package pl.draciel.octocat.core.di.base

import androidx.appcompat.app.AppCompatActivity

abstract class BaseActivity<T> : AppCompatActivity(), ComponentProvider<T> {

}
