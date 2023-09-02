package com.waffle.mymovieapp.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffle.mymovieapp.data.AppRepository
import com.waffle.mymovieapp.data.response.ResultsItem
import com.waffle.mymovieapp.data.response.ResultsItemReview
import retrofit2.HttpException

class DiscoverReviewPagingRepository(private val repository: AppRepository, private val id: Int): PagingSource<Int, ResultsItemReview>() {
    override fun getRefreshKey(state: PagingState<Int, ResultsItemReview>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItemReview> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getDiscoverReview(currentPage, id)
            LoadResult.Page (
                data = response.body()?.results ?: listOf(),
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }
        catch (e: Exception) {
            Log.e("TAG", "load: $e", )
            LoadResult.Error(e)
        }
        catch (exception: HttpException) {
            Log.e("TAG", "load: $exception", )
            LoadResult.Error(exception)
        }
    }

}
