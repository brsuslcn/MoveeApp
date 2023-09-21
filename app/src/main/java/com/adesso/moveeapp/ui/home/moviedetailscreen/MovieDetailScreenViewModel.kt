package com.adesso.moveeapp.ui.home.moviedetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.ui.home.moviedetailscreen.model.MovieDetailsUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailScreenViewModel @Inject constructor(
    private val moviesRepository: MoviesRepository,
    ) : ViewModel() {

    private val _singleMovieInfoFlow =
        MutableStateFlow<DataState<MovieDetailsUiModel>>(DataState.Loading)
    val singleMovieInfoFlow: StateFlow<DataState<MovieDetailsUiModel>> get() = _singleMovieInfoFlow

    fun getSingleMovieInfo(movieId: Int) {
        viewModelScope.launch {
            val movieResponse = moviesRepository.getSingleMovie(movieId)
            val creditsResponse = moviesRepository.getMovieCredits(movieId)

            if (movieResponse is ApiResponse.Success && creditsResponse is ApiResponse.Success) {
                val movieInfo = movieResponse.data
                val creditsInfo = creditsResponse.data

                val transformedData = DataTransformer.transformData(
                    movieInfo,
                    creditsInfo
                ) { movieAllInfo, movieCredits ->
                    MovieDetailsUiModel(
                        id = movieAllInfo.id,
                        name = movieAllInfo.title ?: "",
                        imagePath = movieAllInfo.posterPath ?: "",
                        duration = movieAllInfo.runtime.toString(),
                        releaseDate = DataTransformer.transformSpecialDateFormat(movieAllInfo.releaseDate),
                        genre = movieAllInfo.genres?.joinToString(separator = ", ") { it.name } ?: "",
                        rate = DataTransformer.roundToNearest(movieAllInfo.voteAverage ?: 0.0),
                        overview = movieAllInfo.overview ?: "",
                        directors = movieCredits.crew
                            ?.filter { it.job == "Director" }
                            ?.joinToString { it.name } ?: "",
                        authors = movieCredits.crew
                            ?.filter { it.job == "Author" || it.job == "Writer" }
                            ?.joinToString { it.name } ?: "",
                        stars = movieCredits.cast
                            ?.filter { it.order == 0 || it.order == 1 }
                            ?.joinToString { it.originalName } ?: ""
                    )
                }
                _singleMovieInfoFlow.value = DataState.Success(transformedData)

            } else {
                _singleMovieInfoFlow.value = DataState.Error(Exception("Data cannot be fetched!"))
            }
        }
    }
}