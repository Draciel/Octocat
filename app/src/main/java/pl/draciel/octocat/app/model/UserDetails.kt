package pl.draciel.octocat.app.model

import org.threeten.bp.LocalDateTime


data class UserDetails(
    val id: Long,
    val avatarUrl: String,
    val login: String,
    val nodeId: String,
    val type: String,
    val name: String?,
    val company: String?,
    val blog: String?,
    val location: String?,
    val bio: String?,
    val email: String?,
    val publicRepos: Int,
    val publicGists: Int,
    val followers: Int,
    val following: Int,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
