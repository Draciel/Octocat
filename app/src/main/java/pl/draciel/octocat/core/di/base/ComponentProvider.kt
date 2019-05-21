package pl.draciel.octocat.core.di.base

interface ComponentProvider<T> {
    fun buildComponent(): T
}
