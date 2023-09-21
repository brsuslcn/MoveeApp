package com.adesso.moveeapp.ui.home.profilescreen

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.adesso.moveeapp.data.model.addfavoritesmodel.AddFavoritesBodyModel
import com.adesso.moveeapp.data.model.deletesessionmodel.DeleteSessionBodyModel
import com.adesso.moveeapp.data.repository.AccountRepository
import com.adesso.moveeapp.data.repository.MoviesRepository
import com.adesso.moveeapp.data.repository.TvSeriesRepository
import com.adesso.moveeapp.ui.home.profilescreen.model.FavoriteUiModel
import com.adesso.moveeapp.util.ApiResponse
import com.adesso.moveeapp.util.Constants
import com.adesso.moveeapp.util.DataTransformer
import com.adesso.moveeapp.util.SessionManager
import com.adesso.moveeapp.util.state.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileScreenViewModel @Inject constructor(
    private val accountRepository: AccountRepository,
    private val tvSeriesRepository: TvSeriesRepository,
    private val moviesRepository: MoviesRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    val showDialogState = mutableStateOf(false)
    val lazyColumnRefreshState = mutableStateOf(false)
    private val sessionId = sessionManager.getRegisteredItem(Constants.SESSION_ID)!!
    private val accountId = sessionManager.getRegisteredItem(Constants.PROFILE_ID)?.toInt() ?: 0

    private val _username = MutableStateFlow<DataState<String>>(DataState.Loading)
    val username: StateFlow<DataState<String>> get() = _username

    private val _isLogout = MutableStateFlow(false)
    val isLogout: StateFlow<Boolean> get() = _isLogout

    val favoriteTvFlow: Flow<PagingData<FavoriteUiModel>> =
        accountRepository.getFavoriteTvList(accountId, sessionId)
            .map { pagingData ->
                pagingData.map { favroiteTv ->
                    DataTransformer.transformData(favroiteTv) { tvSeries ->
                        FavoriteUiModel(
                            id = tvSeries.id,
                            imagePath = tvSeries.posterPath ?: "",
                            name = tvSeries.name ?: "",
                            character = getTvCharacter(tvSeries.id),
                            releaseYear = if(!tvSeries.firstAirDate.isNullOrEmpty()) tvSeries.firstAirDate.substring(0, 4) else ""
                        )
                    }
                }
            }.cachedIn(viewModelScope)

    val favoriteMovieFlow: Flow<PagingData<FavoriteUiModel>> =
        accountRepository.getFavoriteMovieList(accountId, sessionId)
            .map { pagingData ->
                pagingData.map { favoriteMovie ->
                    DataTransformer.transformData(favoriteMovie) { movie ->
                        FavoriteUiModel(
                            id = movie.id,
                            imagePath = movie.posterPath ?: "",
                            name = movie.title ?: "",
                            character = getMovieCharacter(movie.id),
                            releaseYear = if(!movie.releaseDate.isNullOrEmpty()) movie.releaseDate.substring(0, 4) else ""
                        )
                    }
                }
            }


    fun getAccountInfo() {
        viewModelScope.launch {
            val apiResponse = accountRepository.getAccountInfo(sessionId)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    val username = apiResponse.data.username
                    _username.value = DataState.Success(username)
                }

                is ApiResponse.Error -> {
                    _username.value = DataState.Error(Exception("Error!!"))
                }

            }
        }
    }

    fun logout() {
        val session = DeleteSessionBodyModel(sessionId)
        viewModelScope.launch {
            val apiResponse = accountRepository.deleteSession(session)

            when (apiResponse) {
                is ApiResponse.Success -> {
                    _isLogout.value = true
                    sessionManager.registerItem(Constants.SESSION_ID, "null")
                }

                is ApiResponse.Error -> {
                    _isLogout.value = false
                }
            }
        }
    }

    fun deleteFavorite(item: AddFavoritesBodyModel) {
        viewModelScope.launch {
            val apiResponse = accountRepository.changeFavorite(accountId, sessionId, item)
            when (apiResponse) {
                is ApiResponse.Success -> {
                    lazyColumnRefreshState.value = true
                }

                is ApiResponse.Error -> {}
            }
        }
    }

    private suspend fun <T> getCharacter(
        id: Int,
        fetchCredits: suspend (Int) -> ApiResponse<T>,
        extractCharacter: (T) -> String
    ): String {
        val apiResponse = fetchCredits(id)

        return when (apiResponse) {
            is ApiResponse.Success -> extractCharacter(apiResponse.data)
            is ApiResponse.Error -> ""
        }
    }

    private suspend fun getTvCharacter(id: Int): String {
        return getCharacter(id, tvSeriesRepository::getTvSeriesCredits) { data ->
            data.cast?.firstOrNull { it.order == 0 }?.character ?: ""
        }
    }

    private suspend fun getMovieCharacter(id: Int): String {
        return getCharacter(id, moviesRepository::getMovieCredits) { data ->
            data.cast?.firstOrNull { it.order == 0 }?.character ?: ""
        }
    }


}