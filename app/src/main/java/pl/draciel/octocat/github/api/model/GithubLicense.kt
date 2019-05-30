package pl.draciel.octocat.github.api.model

import com.google.gson.annotations.SerializedName

data class GithubLicense(
    val key: String,
    val name: String,
    @SerializedName("spdx_id")
    val spdxId: String,
    val url: String,
    @SerializedName("node_id")
    val nodeId: String
)
