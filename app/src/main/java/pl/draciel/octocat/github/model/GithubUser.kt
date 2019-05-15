package pl.draciel.octocat.github.model

import com.google.gson.annotations.SerializedName
import org.threeten.bp.LocalDateTime

internal data class GithubUser(
    val login: String,
    val name: String?,
    val company: String?,
    val location: String?,
    val email: String?,
    val id: String,
    val url: String,
    @SerializedName("repos_url") val reposUrl: String?,
    @SerializedName("created_at") val createdAt: LocalDateTime,
    @SerializedName("updated_at") val updatedAt: LocalDateTime
)
