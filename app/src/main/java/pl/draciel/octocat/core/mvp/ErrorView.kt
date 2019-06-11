package pl.draciel.octocat.core.mvp

import androidx.annotation.StringRes

interface ErrorView {
    fun showError(@StringRes message: Int) {
        // optional, override if needed
    }

    fun showError(message: String) {
        // optional, override if needed
    }

    fun hideError() {
        // optional, override if needed
    }
}
