package com.adesso.moveeapp.util.pagination

import androidx.paging.Pager
import androidx.paging.PagingConfig
import com.adesso.moveeapp.util.Constants


fun <T : Any> createPager(
    pageSize: Int = Constants.DEFAULT_PAGINATION_SIZE,
    enablePlaceholders: Boolean = false,
    block: suspend (Int) -> List<T>
): Pager<Int, T> = Pager(
    config = PagingConfig(enablePlaceholders = enablePlaceholders, pageSize = pageSize),
    pagingSourceFactory = { Pagination(block) }
)