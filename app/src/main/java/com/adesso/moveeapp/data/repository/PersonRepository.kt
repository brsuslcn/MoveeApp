package com.adesso.moveeapp.data.repository


import com.adesso.moveeapp.data.model.actormoviecreditsmodel.ActorMovieCreditsModel
import com.adesso.moveeapp.data.model.actortvcreditsmodel.ActorTvCreditsModel
import com.adesso.moveeapp.data.model.persondetailsmodel.PersonDetailsModel
import com.adesso.moveeapp.data.remote.network.BaseRepository
import com.adesso.moveeapp.data.remote.service.PersonService
import com.adesso.moveeapp.util.ApiResponse
import javax.inject.Inject

class PersonRepository @Inject constructor(private val personService: PersonService) :
    BaseRepository() {
    suspend fun getPersonDetail(personId: Int): ApiResponse<PersonDetailsModel?> {
        return safeApiRequest { personService.getPersonDetail(personId) }
    }

    suspend fun getPersonTvCredits(personId: Int): ApiResponse<ActorTvCreditsModel?> {
        return safeApiRequest { personService.getPersonTVCredits(personId) }
    }

    suspend fun getPersonMovieCredits(personId: Int): ApiResponse<ActorMovieCreditsModel?> {
        return safeApiRequest { personService.getPersonMovieCredits(personId) }
    }
}