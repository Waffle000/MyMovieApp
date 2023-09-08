package com.waffle.mymovieapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.waffle.mymovieapp.data.AppRepository
import com.waffle.mymovieapp.data.response.ResultsItem
import retrofit2.HttpException

class NowPlayingPagingRepository(private val repository: AppRepository): PagingSource<Int, ResultsItem>() {
    override fun getRefreshKey(state: PagingState<Int, ResultsItem>): Int? {
        return null
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ResultsItem> {
        return try {
            val currentPage = params.key ?: 1
            val response = repository.getNowPlayingList(currentPage)
            LoadResult.Page (
                data = response.body()?.results ?: listOf(),
                prevKey = if (currentPage == 1) null else -1,
                nextKey = currentPage.plus(1)
            )
        }
        catch (e: Exception) {
            LoadResult.Error(e)
        }
        catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

}
