package com.adesso.moveeapp.ui.home.tvserieshomescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adesso.moveeapp.data.model.genresmodel.Genre
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.ui.home.tvserieshomescreen.model.TvSeriesPopularUiModel
import com.adesso.moveeapp.ui.home.tvserieshomescreen.model.TvSeriesStateUiModel
import com.adesso.moveeapp.ui.home.tvserieshomescreen.model.TvSeriesTopRatedUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeriesHomeScreenViewModel @Inject constructor(
    private val tvSeriesRepository: TvSeriesRepository,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager

) :
    ViewModel() {

    private val sessionId = sessionManager.getRegisteredItem(Constants.SESSION_ID) ?: ""
    private val _favorites = MutableStateFlow<List<Int>>(emptyList())
    val favorites: StateFlow<List<Int>> = _favorites
    val popularState = mutableStateOf(TvSeriesStateUiModel("", "", ""))

    val tvSeriesPopularFlow: Flow<PagingData<TvSeriesPopularUiModel>> =
        tvSeriesRepository.getPopularTvSeries()
            .map { pagingData ->
                pagingData.map { tvSeries ->
                    DataTransformer.transformData(tvSeries) { tv ->
                        TvSeriesPopularUiModel(
                            id = tv.id,
                            name = tv.name ?: "",
                            posterPath = tv.posterPath ?: "",
                            voteAverage = tv.voteAverage ?: 0.0,
                            genreIds = DataTransformer.transformGenre(
                                tv.genreIds,
                                allGenresFlow.value
                            )
                        )
                    }
                }
            }.cachedIn(viewModelScope)

    val tvSeriesTopRatedFlow: Flow<PagingData<TvSeriesTopRatedUiModel>> =
        tvSeriesRepository.getTopRatedTvSeries()
            .map { pagingData ->
                pagingData.map { tvSeries ->
                    addFavorite(tvSeries.id)

                    DataTransformer.transformData(tvSeries) { tv ->
                        TvSeriesTopRatedUiModel(
                            id = tv.id,
                            imagePath = tv.posterPath ?: "",
                            name = tv.name ?: "",
                            voteAverage = tv.voteAverage ?: 0.0,
                        )
                    }
                }
            }.cachedIn(viewModelScope)

    private val allGenresFlow = MutableStateFlow<List<Genre>>(emptyList())


    init {
        getAllGenres()
    }

    private fun getAllGenres() {
        viewModelScope.launch {
            val apiResponse = tvSeriesRepository.getAllGenres()

            when (apiResponse) {
                is ApiResponse.Success -> {
                    val genres = apiResponse.data.genres
                    allGenresFlow.value = genres
                }

                is ApiResponse.Error -> {
                    DataState.Error(Exception("Genres Error!"))
                }
            }
        }
    }

    private suspend fun getTvFavoriteState(seriesId: Int): Boolean? {
        val apiResponse = accountRepository.getTvState(seriesId, sessionId)

        when (apiResponse) {
            is ApiResponse.Success -> {
                return apiResponse.data.favorite
            }

            is ApiResponse.Error -> {
                return null
            }
        }
    }

    fun checkFavorite() {
        viewModelScope.launch {
            val currentFavorites = _favorites.value.toMutableList()
            val itemsToRemove = mutableListOf<Int>()

            for (seriesId in currentFavorites) {
                val isFavorite = getTvFavoriteState(seriesId) ?: false
                if (!isFavorite) {
                    itemsToRemove.add(seriesId)
                }
            }

            currentFavorites.removeAll(itemsToRemove)
            _favorites.value = currentFavorites
        }
    }

    fun addFavorite(seriesId: Int): Deferred<Unit> =
        viewModelScope.async {
            val isFavorite = getTvFavoriteState(seriesId) ?: false

            val currentFavorites = _favorites.value.toMutableList()
            if (isFavorite && !currentFavorites.contains(seriesId)) {
                currentFavorites.add(seriesId)
                _favorites.value = currentFavorites
            }
        }
}