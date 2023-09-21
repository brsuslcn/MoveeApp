package com.adesso.moveeapp.ui.home.movieshomescreen


import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adesso.moveeapp.data.model.genresmodel.Genre
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.ui.home.movieshomescreen.model.MoviesPlayingNowUiModel
import com.adesso.moveeapp.ui.home.movieshomescreen.model.MoviesPopularUiModel
import com.adesso.moveeapp.ui.home.movieshomescreen.model.MoviesStateUiModel
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
class MoviesHomeScreenViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    private val accountRepository: AccountRepository,
    private val sessionManager: SessionManager
) : ViewModel() {
    init {
        getAllGenres()
    }

    val playingMovieState = mutableStateOf(MoviesStateUiModel("", "", ""))

    private val sessionId = sessionManager.getRegisteredItem(Constants.SESSION_ID) ?: ""

    private val _favorites = MutableStateFlow<List<Int>>(emptyList())
    val favorites: StateFlow<List<Int>> = _favorites

    val moviesPlayNowFlow: Flow<PagingData<MoviesPlayingNowUiModel>> =
        moviesRepository.getPlayingMovies()
            .map { pagingData ->
                pagingData.map { playingNowMovie ->
                    DataTransformer.transformData(playingNowMovie) { movie ->
                        MoviesPlayingNowUiModel(
                            id = movie.id,
                            name = movie.title ?: "",
                            imagePath = movie.posterPath ?: "",
                            voteAverage = movie.voteAverage ?: 0.0,
                            genre = DataTransformer.transformGenre(
                                movie.genreIds ?: emptyList(),
                                allGenresFlow.value
                            )
                        )
                    }
                }
            }.cachedIn(viewModelScope)

    val moviesFlow: Flow<PagingData<MoviesPopularUiModel>> =
        moviesRepository.getPopularMovies()
            .map { pagingData ->
                pagingData.map { popularMovie ->
                    addFavorite(popularMovie.id)

                    DataTransformer.transformData(popularMovie) { movie ->
                        MoviesPopularUiModel(
                            id = movie.id,
                            name = movie.title ?: "",
                            imagePath = movie.posterPath ?: "",
                            voteAverage = movie.voteAverage ?: 0.0,
                            date = DataTransformer.transformSpecialDateFormat(movie.releaseDate),
                            genre = DataTransformer.transformGenre(
                                movie.genreIds ?: emptyList(),
                                allGenresFlow.value
                            ),
                        )
                    }
                }
            }.cachedIn(viewModelScope)

    private val allGenresFlow = MutableStateFlow<List<Genre>>(emptyList())

    fun checkFavorite() {
        viewModelScope.launch {
            val currentFavorites = _favorites.value.toMutableList()
            val itemsToRemove = mutableListOf<Int>()

            for (movieId in currentFavorites) {
                val isFavorite = getMovieFavoriteState(movieId) ?: false
                if (!isFavorite) {
                    itemsToRemove.add(movieId)
                }
            }

            currentFavorites.removeAll(itemsToRemove)
            _favorites.value = currentFavorites
        }
    }

    fun addFavorite(movieId: Int): Deferred<Unit> =
        viewModelScope.async {
            val isFavorite = getMovieFavoriteState(movieId) ?: false

            val currentFavorites = _favorites.value.toMutableList()
            if (isFavorite && !currentFavorites.contains(movieId)) {
                currentFavorites.add(movieId)
                _favorites.value = currentFavorites
            }
        }

    private fun getAllGenres() {
        viewModelScope.launch {
            val apiResponse = moviesRepository.getAllGenres()

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


    private suspend fun getMovieFavoriteState(movieId: Int): Boolean? {
        val apiResponse = accountRepository.getMovieState(movieId, sessionId)

        when (apiResponse) {
            is ApiResponse.Success -> {
                return apiResponse.data.favorite
            }

            is ApiResponse.Error -> {
                return null
            }
        }
    }
}
