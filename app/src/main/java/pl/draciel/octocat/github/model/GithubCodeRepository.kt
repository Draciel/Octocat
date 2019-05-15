package pl.draciel.octocat.github.model

import org.threeten.bp.LocalDateTime


internal data class GithubCodeRepository(
    val repositoryName: String,
    val url: String,
    val description: String,
    val pushedAt: LocalDateTime,
    val language: String,
    val watchers: Int,
    val forks: Int
)
