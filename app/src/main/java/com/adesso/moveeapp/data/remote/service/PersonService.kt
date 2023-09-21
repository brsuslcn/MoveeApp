package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.actormoviecreditsmodel.ActorMovieCreditsModel
import com.adesso.moveeapp.data.model.persondetailsmodel.PersonDetailsModel
import com.adesso.moveeapp.data.model.actortvcreditsmodel.ActorTvCreditsModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface PersonService {
    @GET(Constants.PERSON_DETAILS_PATH)
    suspend fun getPersonDetail(
        @Path("person_id") personId: Int
    ): Response<PersonDetailsModel>

    @GET(Constants.PERSON_TV_CREDITS_PATH)
    suspend fun getPersonTVCredits(
        @Path("person_id") personId: Int
    ): Response<ActorTvCreditsModel>

    @GET(Constants.PERSON_MOVIE_CREDITS_PATH)
    suspend fun getPersonMovieCredits(
        @Path("person_id") personId: Int
    ): Response<ActorMovieCreditsModel>
}