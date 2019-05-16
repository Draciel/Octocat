package pl.draciel.octocat.core.utility

interface DeleteListener<T> {
    fun onDelete(item: T)
}
