package pl.draciel.octocat.core.utility

interface SaveListener<T> {
    fun onSave(item: T)
}
