package pl.draciel.octocat.app.model

data class User(
    val login: String,
    val id: Long,
    val type: String,
    val avatarUrl: String,
    val score: Double
)
