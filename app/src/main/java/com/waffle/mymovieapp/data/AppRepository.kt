package com.waffle.mymovieapp.data

import com.waffle.mymovieapp.api.ApiService
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.data.response.DiscoverResponse
import com.waffle.mymovieapp.data.response.DiscoverReviewResponse
import com.waffle.mymovieapp.data.response.DiscoverThrillerResponse
import com.waffle.mymovieapp.data.response.GenreResponse
import com.waffle.mymovieapp.data.response.PopularResponse
import retrofit2.Response

class AppRepository(private val apiService: ApiService) {

    suspend fun getGenreList(): Response<GenreResponse> {
        return apiService.getGenreList()
    }
    suspend fun getDiscoverList(page: Int, genre: Int): Response<DiscoverResponse> {
        return apiService.getDiscoverList(
            page,
            sortBy = "popularity.desc",
            genre = genre
        )
    }

    suspend fun getDiscoverDetail(id: Int): Response<DiscoverDetailResponse> {
        return apiService.getDiscoverDetail(id)
    }

    suspend fun getDiscoverReview(page: Int, id: Int): Response<DiscoverReviewResponse> {
        return apiService.getDiscoverReview(page= page, id = id)
    }

    suspend fun getDiscoverTrailer(id: Int): Response<DiscoverThrillerResponse> {
        return apiService.getDiscoverTrailer(id)
    }

    suspend fun getPopularList() : Response<PopularResponse> {
        return apiService.getPopularList()
    }

}