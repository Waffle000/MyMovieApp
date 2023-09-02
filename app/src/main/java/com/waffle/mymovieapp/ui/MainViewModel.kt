package com.waffle.mymovieapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.waffle.mymovieapp.data.AppRepository
import com.waffle.mymovieapp.data.response.DiscoverDetailResponse
import com.waffle.mymovieapp.data.response.DiscoverThrillerResponse
import com.waffle.mymovieapp.data.response.GenreResponse
import com.waffle.mymovieapp.data.response.PopularResponse
import com.waffle.mymovieapp.data.response.ResultsItem
import com.waffle.mymovieapp.data.response.ResultsItemReview
import com.waffle.mymovieapp.paging.DiscoverPagingRepository
import com.waffle.mymovieapp.paging.DiscoverReviewPagingRepository
import com.waffle.mymovieapp.utils.SingleLiveEvent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import okio.IOException

class MainViewModel(private val repository: AppRepository): ViewModel() {

    private val getGenreSuccess = MutableLiveData<SingleLiveEvent<GenreResponse>>()
    fun observeGetGenreSuccess() : LiveData<SingleLiveEvent<GenreResponse>> = getGenreSuccess

    private val getDiscoverDetailSuccess = MutableLiveData<SingleLiveEvent<DiscoverDetailResponse>>()
    fun observeGetDiscoverDetailSuccess() : LiveData<SingleLiveEvent<DiscoverDetailResponse>> = getDiscoverDetailSuccess

    private val getDiscoverThrillerSuccess = MutableLiveData<SingleLiveEvent<DiscoverThrillerResponse>>()
    fun observeGetDiscoverThrillerSuccess() : LiveData<SingleLiveEvent<DiscoverThrillerResponse>> = getDiscoverThrillerSuccess

    private val getPopularListSuccess = MutableLiveData<SingleLiveEvent<PopularResponse>>()
    fun observeGetPopularListSuccess() : LiveData<SingleLiveEvent<PopularResponse>> = getPopularListSuccess

    private val isError = MutableLiveData<SingleLiveEvent<String>>()
    fun observeIsError() : LiveData<SingleLiveEvent<String>> = isError
    fun getGenreList() {
        viewModelScope.launch {
            try {
                val result = repository.getGenreList().body()
                if (result != null) {
                    getGenreSuccess.postValue(SingleLiveEvent(result))
                } else {
                   isError.postValue(SingleLiveEvent("Data Kosong"))
                }
            } catch (e: Exception) {
               isError.postValue(SingleLiveEvent(e.message ?: "Terjadi Kesalahan"))
            }
        }
    }
    fun getDiscoverList(genre: Int) : Flow<PagingData<ResultsItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                DiscoverPagingRepository(
                    repository, genre
                )
            }
        ).flow.cachedIn(viewModelScope)

    }

    fun getDiscoverDetail(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getDiscoverDetail(id).body()
                if (result != null) {
                    getDiscoverDetailSuccess.postValue(SingleLiveEvent(result))
                } else {
                    isError.postValue(SingleLiveEvent("Data Kosong"))
                }
            } catch (e: Exception) {
                isError.postValue(SingleLiveEvent(e.message ?: "Terjadi Kesalahan"))
            }
        }
    }

    fun getDiscoverReview(id: Int) : Flow<PagingData<ResultsItemReview>> {
        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 10
            ),
            pagingSourceFactory = {
                DiscoverReviewPagingRepository(
                    repository, id
                )
            }
        ).flow.cachedIn(viewModelScope)
    }

    fun getDiscoverTrailer(id: Int) {
        viewModelScope.launch {
            try {
                val result = repository.getDiscoverTrailer(id).body()
                if (result != null) {
                    getDiscoverThrillerSuccess.postValue(SingleLiveEvent(result))
                } else {
                    isError.postValue(SingleLiveEvent("Data Kosong"))
                }
            } catch (e: Exception) {
                isError.postValue(SingleLiveEvent(e.message ?: "Terjadi Kesalahan"))
            }
        }
    }

    fun getPopularList() {
        viewModelScope.launch {
            try {
                val result = repository.getPopularList().body()
                if (result != null) {
                    getPopularListSuccess.postValue(SingleLiveEvent(result))
                } else {
                    isError.postValue(SingleLiveEvent("Data Kosong"))
                }
            } catch (e: Exception) {
                Log.e("TAG", "getPopularList: $e", )
                isError.postValue(SingleLiveEvent(e.message ?: "Terjadi Kesalahan"))
            } catch (t: Throwable) {
                Log.e("TAG", "getPopularList: $t", )
            }
        }
    }
}