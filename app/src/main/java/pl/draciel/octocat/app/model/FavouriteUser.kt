package pl.draciel.octocat.app.model

data class FavouriteUser(
    val login: String,
    val id: Long,
    val type: String,
    val avatarUrl: String,
    val company: String?,
    val bio: String?
)
