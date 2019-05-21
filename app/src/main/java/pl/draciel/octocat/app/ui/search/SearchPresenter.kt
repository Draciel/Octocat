package pl.draciel.octocat.app.ui.search

import pl.draciel.octocat.core.mvp.BaseLifecyclePresenter
import pl.draciel.octocat.github.GithubRepository

internal class SearchPresenter(private val githubRepository: GithubRepository) :
    BaseLifecyclePresenter<SearchMVP.View>(),
    SearchMVP.Presenter {


}
