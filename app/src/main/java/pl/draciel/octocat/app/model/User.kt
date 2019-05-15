package pl.draciel.octocat.app.model

import org.threeten.bp.LocalDateTime


data class User(
    val login: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val id: String,
    val url: String,
    val repoUrl: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
