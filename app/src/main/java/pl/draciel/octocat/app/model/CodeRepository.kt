package pl.draciel.octocat.app.model

import org.threeten.bp.LocalDateTime

data class CodeRepository(
    val repositoryName: String,
    val url: String,
    val description: String,
    val pushedAt: LocalDateTime,
    val language: String,
    val watchers: Int,
    val forks: Int,
    val id: Long
)
