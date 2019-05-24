package pl.draciel.octocat.github.api.model

import com.google.gson.annotations.SerializedName

internal data class SearchUserResponse(
    @SerializedName("total_count") val totalCount: Int,
    @SerializedName("incomplete_results") val incompleteResults: Boolean,
    @SerializedName("items") val users: List<GithubUser>
)
