package com.adesso.moveeapp.ui.home.tvseriesdetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.ui.home.tvseriesdetailscreen.model.CastUiModel
import com.adesso.moveeapp.ui.home.tvseriesdetailscreen.model.TvSeriesDetailsUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvSeriesDetailScreenViewModel @Inject constructor(private val tvSeriesRepository: TvSeriesRepository) :
    ViewModel() {
    private val _tvSeriesInfoFlow =
        MutableStateFlow<DataState<TvSeriesDetailsUiModel>>(DataState.Loading)
    val tvSeriesInfoFlow: StateFlow<DataState<TvSeriesDetailsUiModel>> get() = _tvSeriesInfoFlow

    private val _castFlow = MutableStateFlow<DataState<List<CastUiModel>>>(DataState.Loading)
    val castFlow: StateFlow<DataState<List<CastUiModel>>> get() = _castFlow


    fun getTvSeriesInfo(seriesId: Int) {
        viewModelScope.launch {
            val apiResponse = tvSeriesRepository.getTvSeriesInfo(seriesId)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    val tvSeriesInfo = apiResponse.data

                    val transformedData = DataTransformer.transformData(tvSeriesInfo) { source ->
                        TvSeriesDetailsUiModel(
                            id = source.id,
                            imagePath = source.posterPath ?: "",
                            name = source.name,
                            overview = source.overview ?: "",
                            rate = DataTransformer.roundToNearest(source.voteAverage ?: 0.0),
                            genre = source.genres.joinToString(separator = ", ") { it.name },
                            creators = source.createdBy.joinToString(separator = ", ") { it.name },
                            seasons = (source.seasons?.size ?: "").toString(),
                            releaseDate = DataTransformer.transformSpecialDateFormat(source.firstAirDate),
                            duration = source.episodeRunTime.joinToString()
                        )
                    }
                    _tvSeriesInfoFlow.value = DataState.Success(transformedData)
                }

                is ApiResponse.Error -> {
                    _tvSeriesInfoFlow.value = DataState.Error(Exception("Data cannot be fetched!"))
                }
            }
        }
    }

    fun getCast(tvSeriesId: Int) {
        viewModelScope.launch {
            val apiResponse = tvSeriesRepository.getTvSeriesCredits(tvSeriesId)
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val cast = apiResponse.data.cast
                    val transformedCast =
                        DataTransformer.transformData(cast) { casts ->
                            casts?.map { castItem ->
                                CastUiModel(
                                    id = castItem.id,
                                    name = castItem.name,
                                    profilePath = castItem.profilePath
                                )

                            }
                        }
                    _castFlow.value = DataState.Success(transformedCast ?: emptyList())
                }

                is ApiResponse.Error -> {
                    _castFlow.value = DataState.Error(Exception("Data cannot be fetched!"))
                }
            }
        }
    }
}