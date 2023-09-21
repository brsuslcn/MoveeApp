package com.adesso.moveeapp.ui.home.actordetailscreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.data.repository.PersonRepository
import com.adesso.moveeapp.ui.home.actordetailscreen.model.ActorCreditUiModel
import com.adesso.moveeapp.ui.home.actordetailscreen.model.ActorUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActorDetailScreenViewModel @Inject constructor(private val personRepository: PersonRepository) :
    ViewModel() {
    private val _actorDetailFlow = MutableStateFlow<DataState<ActorUiModel>>(DataState.Loading)
    val actorDetailFlow: StateFlow<DataState<ActorUiModel>> get() = _actorDetailFlow

    private val _tvCreditsFlow =
        MutableStateFlow<DataState<List<ActorCreditUiModel>>>(DataState.Loading)
    val tvCreditsFlow: StateFlow<DataState<List<ActorCreditUiModel>>> get() = _tvCreditsFlow

    private val _movieCreditsFlow =
        MutableStateFlow<DataState<List<ActorCreditUiModel>>>(DataState.Loading)
    val movieCreditsFlow: StateFlow<DataState<List<ActorCreditUiModel>>> get() = _movieCreditsFlow

    fun getActorDetail(actorId: Int) {
        viewModelScope.launch {
            val apiResponse = personRepository.getPersonDetail(actorId)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    val person = apiResponse.data
                    val transformToActor = DataTransformer.transformData(person) { actor ->
                        ActorUiModel(
                            image = actor?.profilePath ?: "",
                            name = actor?.name ?: "",
                            biography = actor?.biography ?: "",
                            born = DataTransformer.concatBornStatus(
                                birthDate = person?.birthday ?: "",
                                placeOfBirth = person?.placeOfBirth ?: ""
                            )
                        )

                    }
                    _actorDetailFlow.value = DataState.Success(transformToActor)
                }

                is ApiResponse.Error -> {
                    _actorDetailFlow.value = DataState.Error(Exception("Data cannot be fetched!"))
                }
            }
        }
    }

    fun getTvCredits(actorId: Int) {
        viewModelScope.launch {
            val apiResponse = personRepository.getPersonTvCredits(actorId)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    val tvCredits = apiResponse.data
                    val transformedCredit =
                        DataTransformer.transformData(tvCredits?.castTv ?: emptyList()) { casts ->
                            casts.map { cast ->
                                ActorCreditUiModel(
                                    imagePath = cast.posterPath,
                                    name = cast.name,
                                    voteAverage = DataTransformer.roundToNearest(cast.voteAverage),
                                    date = DataTransformer.transformSpecialDateFormat(cast.firstAirDate),
                                    id = cast.id
                                )
                            }
                        }
                    _tvCreditsFlow.value = DataState.Success(transformedCredit)
                }

                is ApiResponse.Error -> {
                    _tvCreditsFlow.value = DataState.Error(Exception("TV List cannot be showed!"))
                }
            }
        }
    }

    fun getMovieCredits(actorId: Int) {
        viewModelScope.launch {
            val apiResponse = personRepository.getPersonMovieCredits(actorId)
            when (apiResponse) {
                is ApiResponse.Success -> {
                    val movieCredits = apiResponse.data

                    val transformedCredit = DataTransformer.transformData(
                        movieCredits?.castMovie ?: emptyList()
                    ) { movieCredit ->
                        movieCredit.map { cast ->
                            ActorCreditUiModel(
                                imagePath = cast.posterPath,
                                name = cast.title,
                                voteAverage = DataTransformer.roundToNearest(cast.voteAverage),
                                date = DataTransformer.transformSpecialDateFormat(cast.releaseDate),
                                id = cast.id
                            )
                        }
                    }
                    _movieCreditsFlow.value = DataState.Success(transformedCredit)
                }

                is ApiResponse.Error -> {
                    _movieCreditsFlow.value =
                        DataState.Error(Exception("Movie List cannot be showed!"))
                }
            }
        }
    }
}