package com.adesso.moveeapp.ui.home.searchscreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.filter
import androidx.paging.map
import com.adesso.moveeapp.data.model.creditsmodel.CreditsModel
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.data.repository.PersonRepository
import com.adesso.moveeapp.data.repository.SearchRepository
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.ui.home.searchscreen.model.MediaTypeUiModel
import com.adesso.moveeapp.ui.home.searchscreen.model.SearchUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.DataTransformer
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class SearchScreenViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val tvRepository: TvSeriesRepository,
    private val moviesRepository: MoviesRepository,
    private val personRepository: PersonRepository
) : ViewModel() {
    var searchState = mutableStateOf("")

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: Flow<String> = _searchQuery.asStateFlow()

    val searchResultFlow: Flow<PagingData<SearchUiModel>> = searchQuery.flatMapLatest { query ->
        searchRepository.searchItems(query).map { pagingData ->
            pagingData.filter { item ->
                item.mediaType in listOf(Constants.MOVIE, Constants.TV, Constants.PERSON)
            }.map { searchItem ->
                DataTransformer.transformData(searchItem) { item ->
                    val mediaTypeUiModel = getMediaTypeUiModel(
                        item.mediaType
                    )
                    SearchUiModel(
                        itemName = mediaTypeUiModel.getItemName.invoke(item.name, item.title) ?: "",
                        itemImage = mediaTypeUiModel.getItemImage.invoke(item.profilePath, item.posterPath) ?: "",
                        itemDescription = getDescription(mediaTypeUiModel, item.id),
                        itemType = item.mediaType,
                        itemId = item.id,
                    )
                }
            }
        }
    }.cachedIn(viewModelScope)


    fun setQuery() {
        _searchQuery.value = searchState.value
    }

    private suspend fun getDescription(description: MediaTypeUiModel, id: Int): String {
        return when (description.type) {
            Constants.PERSON -> getPersonBorn(id)
            Constants.TV -> getTvCast(id)
            Constants.MOVIE -> getMovieCast(id)
            else -> ""
        }
    }

    /**
     * General function for casts
     *
     * @param id the id of tv series or movie
     * @param fetchCredits the function of tvseriescredits or moviecredits
     *
     * @return casts String
     */
    private suspend fun getCast(
        id: Int, fetchCredits: suspend (Int) -> ApiResponse<CreditsModel>
    ): String {
        val apiResponse = fetchCredits(id)
        return when (apiResponse) {
            is ApiResponse.Success -> {
                val cast = apiResponse.data.cast
                val casts = cast?.filter { it.order == 0 || it.order == 1 }?.joinToString { it.name } ?: ""
                casts
            }

            is ApiResponse.Error -> ""
        }
    }

    private suspend fun getTvCast(seriesId: Int): String {
        return getCast(seriesId, tvRepository::getTvSeriesCredits)
    }

    private suspend fun getMovieCast(movieId: Int): String {
        return getCast(movieId, moviesRepository::getMovieCredits)
    }

    private suspend fun getPersonBorn(personId: Int): String {
        val apiResponse = personRepository.getPersonDetail(personId)

        when (apiResponse) {
            is ApiResponse.Success -> {
                val birthDate = apiResponse.data?.birthday
                val placeOfBirth = apiResponse.data?.placeOfBirth
                return DataTransformer.concatBornStatus(birthDate ?: "", placeOfBirth ?: "")
            }

            is ApiResponse.Error -> {
                return ""
            }
        }
    }

    fun getMediaTypeUiModel(type: String): MediaTypeUiModel {
        return when (type) {
            Constants.MOVIE -> MediaTypeUiModel.Movie
            Constants.TV -> MediaTypeUiModel.TvSeries
            Constants.PERSON -> MediaTypeUiModel.Person
            else -> throw IllegalArgumentException("Unknown media type: $type")
        }
    }
}





