package com.waffle.mymovieapp.api

import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.data.response.DiscoverResponse
import com.waffle.mymovieapp.data.response.DiscoverReviewResponse
import com.waffle.mymovieapp.data.response.DiscoverThrillerResponse
import com.waffle.mymovieapp.data.response.GenreResponse
import com.waffle.mymovieapp.data.response.PopularResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("genre/movie/list")
    suspend fun getGenreList() : Response<GenreResponse>

    @GET("discover/movie")
    suspend fun getDiscoverList(
        @Query("page") page: Int,
        @Query("sort_by") sortBy: String,
        @Query("with_genres") genre: Int
    ) : Response<DiscoverResponse>

    @GET("movie/{id}")
    suspend fun getDiscoverDetail(
        @Path("id") id: Int
    ) : Response<DiscoverDetailResponse>

    @GET("movie/{id}/reviews")
    suspend fun getDiscoverReview(
        @Path("id") id: Int,
        @Query("page") page: Int
    ) : Response<DiscoverReviewResponse>

    @GET("movie/{id}/videos")
    suspend fun getDiscoverTrailer(
        @Path("id") id: Int
    ) : Response<DiscoverThrillerResponse>

    @GET("movie/popular")
    suspend fun getPopularList() : Response<PopularResponse>
}