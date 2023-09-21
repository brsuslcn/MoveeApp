package com.adesso.moveeapp.data.remote.service

object Constants {
    const val NEW_REQUEST_TOKEN_PATH = "/3/authentication/token/new"
    const val LOGIN_VALIDATE_PATH = "/3/authentication/token/validate_with_login"
    const val GET_SESSION_ID_PATH =  "/3/authentication/session/new"

    const val NOW_PLAYING_PATH = "/3/movie/now_playing"
    const val POPULAR_PATH = "/3/movie/popular"
    const val MOVIES_ALL_GENRES_PATH = "/3/genre/movie/list"
    const val SINGLE_MOVIE_PATH = "/3/movie/{movie_id}"
    const val MOVIE_CREDITS_PATH = "/3/movie/{movie_id}/credits"
    const val ADD_MOVIE_RATING_PATH = "/3/movie/{movie_id}/rating"

    const val POPULAR_TV_SERIES_PATH = "/3/tv/popular"
    const val TOP_RATED_TV_SERIES_PATH = "/3/tv/top_rated"
    const val TV_SERIES_ALL_GENRES_PATH = "/3/genre/tv/list"
    const val SINGLE_TV_SERIES_PATH = "/3/tv/{series_id}"
    const val TV_CREDITS_PATH = "/3/tv/{series_id}/credits"
    const val ADD_TV_RATING_PATH = "/3/tv/{series_id}/rating"

    const val PERSON_DETAILS_PATH = "/3/person/{person_id}"
    const val PERSON_TV_CREDITS_PATH = "/3/person/{person_id}/tv_credits"
    const val PERSON_MOVIE_CREDITS_PATH = "/3/person/{person_id}/movie_credits"

    const val SEARCH_MULTI_PATH = "/3/search/multi"

    const val ACCOUNT_DETAIL_PATH = "/3/account"
    const val ACCOUNT_DELETE_SESSION_PATH = "/3/authentication/session"
    const val ACCOUNT_ADD_FAVORITE_PATH = "/3/account/{account_id}/favorite"
    const val ACCOUNT_TV_FAVORITE_LIST_PATH = "/3/account/{account_id}/favorite/tv"
    const val ACCOUNT_MOVIE_FAVORITE_LIST_PATH = "/3/account/{account_id}/favorite/movies"
    const val ACCOUNT_STATE_MOVIE_PATH = "/3/movie/{movie_id}/account_states"
    const val ACCOUNT_STATE_TV_PATH = "/3/tv/{series_id}/account_states"
}