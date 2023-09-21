package com.adesso.moveeapp.data.di

import com.adesso.moveeapp.BuildConfig
import com.adesso.moveeapp.data.remote.network.AuthInterceptor
import com.adesso.moveeapp.data.remote.service.AccountService
import com.adesso.moveeapp.data.remote.service.AuthService
import com.adesso.moveeapp.data.remote.service.MoviesService
import com.adesso.moveeapp.data.remote.service.PersonService
import com.adesso.moveeapp.data.remote.service.SearchService
import com.adesso.moveeapp.data.remote.service.TvSeriesService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {
    @Provides
    @Singleton
    fun provideAuthInterceptor(): AuthInterceptor{
        return AuthInterceptor(BuildConfig.API_KEY)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(authInterceptor: AuthInterceptor): OkHttpClient{
        return OkHttpClient.Builder()
            .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC })
            .addInterceptor(authInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient) : Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    @Provides
    @Singleton
    fun provideAuthService(retrofit: Retrofit) : AuthService = retrofit.create(
        AuthService::class.java
    )

    @Provides
    @Singleton
    fun provideMoviesService(retrofit: Retrofit) : MoviesService = retrofit.create(
        MoviesService::class.java
    )

    @Provides
    @Singleton
    fun provideTvSeriesService(retrofit: Retrofit) : TvSeriesService = retrofit.create(
        TvSeriesService::class.java
    )

    @Provides
    @Singleton
    fun providePersonService(retrofit: Retrofit) : PersonService = retrofit.create(
        PersonService::class.java
    )

    @Provides
    @Singleton
    fun provideSearchService(retrofit: Retrofit) : SearchService = retrofit.create(
        SearchService::class.java
    )

    @Provides
    @Singleton
    fun provideAccountService(retrofit: Retrofit) : AccountService = retrofit.create(
        AccountService::class.java
    )
}