package com.adesso.moveeapp.data.repository

import androidx.paging.PagingData
import com.adesso.moveeapp.data.model.searchmodel.Result
import com.adesso.moveeapp.data.remote.service.SearchService
import com.adesso.moveeapp.util.pagination.createPager
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SearchRepository @Inject constructor(private val searchService: SearchService) {
    fun searchItems(query: String) : Flow<PagingData<Result>> = createPager { page ->
        searchService.searchMulti(page, query).body()!!.results
    }.flow
}