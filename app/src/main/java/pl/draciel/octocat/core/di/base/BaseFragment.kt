package pl.draciel.octocat.core.di.base

import androidx.fragment.app.Fragment

abstract class BaseFragment<T> : Fragment(), ComponentProvider<T>


