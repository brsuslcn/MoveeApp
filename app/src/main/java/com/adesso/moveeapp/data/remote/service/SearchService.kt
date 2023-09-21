package com.adesso.moveeapp.data.remote.service

import com.adesso.moveeapp.data.model.searchmodel.SearchModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface SearchService {

    @GET(Constants.SEARCH_MULTI_PATH)
    suspend fun searchMulti(
       @Query("page") page: Int,
       @Query("query") query: String
    ): Response<SearchModel>
}