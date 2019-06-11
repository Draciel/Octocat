package pl.draciel.octocat.core.mvp

interface ProgressView {
    fun showProgress()
    fun hideProgress()
    fun updateProgress(value: Int) {
        // optional, override if needed
    }
}
