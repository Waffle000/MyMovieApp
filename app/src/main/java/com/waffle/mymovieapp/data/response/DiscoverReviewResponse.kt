package com.waffle.mymovieapp.data.response

import kotlinx.parcelize.Parcelize
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

@Parcelize
data class DiscoverReviewResponse(

	@field:SerializedName("id")
	val id: Int? = null,

	@field:SerializedName("page")
	val page: Int? = null,

	@field:SerializedName("total_pages")
	val totalPages: Int? = null,

	@field:SerializedName("results")
	val results: List<ResultsItemReview>? = null,

	@field:SerializedName("total_results")
	val totalResults: Int? = null
) : Parcelable

@Parcelize
data class AuthorDetails(

	@field:SerializedName("avatar_path")
	val avatarPath: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("rating")
	val rating: String? = null,

	@field:SerializedName("username")
	val username: String? = null
) : Parcelable

@Parcelize
data class ResultsItemReview(

	@field:SerializedName("author_details")
	val authorDetails: AuthorDetails? = null,

	@field:SerializedName("updated_at")
	val updatedAt: String? = null,

	@field:SerializedName("author")
	val author: String? = null,

	@field:SerializedName("created_at")
	val createdAt: String? = null,

	@field:SerializedName("id")
	val id: String? = null,

	@field:SerializedName("content")
	val content: String? = null,

	@field:SerializedName("url")
	val url: String? = null
) : Parcelable
