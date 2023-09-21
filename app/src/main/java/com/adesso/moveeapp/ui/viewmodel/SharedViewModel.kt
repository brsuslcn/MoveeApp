package com.adesso.moveeapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adesso.moveeapp.R
import com.adesso.moveeapp.data.model.accountstatemodel.AccountStateModel
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingBodyModel
import com.adesso.moveeapp.data.model.addratingmodel.AddRatingModel
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class SharedViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val moviesRepository: MoviesRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _voteState = MutableStateFlow(0)
    val voteState: StateFlow<Int> = _voteState

    private val _isFavorite = MutableStateFlow(false)
    val isFavorite: StateFlow<Boolean> get() = _isFavorite

    private val _addedFavoriteState = MutableStateFlow<DataState<Int>>(DataState.Initial)
    val isAddedFavoriteState: StateFlow<DataState<Int>> get() = _addedFavoriteState


    private val accountId = sessionManager.getRegisteredItem(Constants.PROFILE_ID)?.toInt() ?: 0
    private val sessionId = sessionManager.getRegisteredItem(Constants.SESSION_ID) ?: ""


    private val stateFunctions = mapOf(
        Constants.MOVIE to accountRepository::getMovieState,
        Constants.TV to accountRepository::getTvState
    )

    fun changeFavorite(itemId: Int, mediaType: String) {

        val item = AddFavoritesBodyModel(mediaType, itemId, !_isFavorite.value)
        viewModelScope.launch {
            val apiResponse = accountRepository.changeFavorite(accountId, sessionId, item)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    _addedFavoriteState.value = DataState.Success( data =
                        if(!_isFavorite.value) {
                            R.string.detail_screens_added_favorite_success
                        } else {
                            R.string.detail_screens_delete_favorite_success
                        }

                    )
                    stateFunctions[mediaType]?.let { getAccountState(itemId, it) }
                }

                is ApiResponse.Error -> {
                    _addedFavoriteState.value = DataState.Error(Exception("An error occurred!"))
                }
            }
        }
    }

    fun getMovieVoteState(movieId: Int) {
        getAccountState(movieId, accountRepository::getMovieState)
    }

    fun getTvVoteState(seriesId: Int) {
        getAccountState(seriesId, accountRepository::getTvState)
    }


    fun voteMovie(movieId: Int, vote: Int) {
        vote(movieId, vote, moviesRepository::voteMovie)
    }

    fun voteTv(seriesId: Int, vote: Int) {
        vote(seriesId, vote, tvSeriesRepository::voteTv)
    }

    private fun vote(
        itemId: Int,
        vote: Int,
        voteFunction: suspend (Int, String, AddRatingBodyModel) -> ApiResponse<AddRatingModel>
    ) {
        viewModelScope.launch {
            val voteBody = AddRatingBodyModel(vote * 2)
            val apiResponse = voteFunction(itemId, sessionId, voteBody)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    if (apiResponse.data.success) {
                        _voteState.value = vote
                    }
                }

                is ApiResponse.Error -> {

                }
            }

        }
    }

    private fun getAccountState(
        itemId: Int,
        stateFunction: suspend (Int, String) -> ApiResponse<AccountStateModel>
    ) {
        viewModelScope.launch {
            val apiResponse = stateFunction(itemId, sessionId)
            getVoteState(apiResponse)
            getFavoriteState(apiResponse)
        }
    }

    private fun getVoteState(apiResponse: ApiResponse<AccountStateModel>) {
        when (apiResponse) {
            is ApiResponse.Success -> {
                val rate = apiResponse.data.rated
                if (rate is Map<*, *>) {
                    val value = rate["value"] as? Double
                    val transformedValue = value?.toInt() ?: 0
                    _voteState.value = transformedValue / 2
                } else {
                    _voteState.value = 0
                }
            }

            is ApiResponse.Error -> {}
        }
    }

    private fun getFavoriteState(apiResponse: ApiResponse<AccountStateModel>) {
        when (apiResponse) {
            is ApiResponse.Success -> {
                val isFavorite = apiResponse.data.favorite ?: false
                _isFavorite.value = isFavorite
            }

            is ApiResponse.Error -> {
                _isFavorite.value = false
            }
        }
    }
}