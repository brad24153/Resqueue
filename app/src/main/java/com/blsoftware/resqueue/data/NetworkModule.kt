package com.blsoftware.resqueue.data

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.blsoftware.resqueue.data.remote.ShelterluvApi
import com.blsoftware.resqueue.BuildConfig
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import javax.inject.Singleton
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val API_KEY = BuildConfig.SHELTERLUV_API_KEY

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        // Interceptor to automatically add the API key header to every request
        val authInterceptor = Interceptor { chain ->
            val originalRequest = chain.request()
            val requestWithHeader = originalRequest.newBuilder()
                .header(
                    "Authorization",
                    "Bearer $API_KEY"
                )
                .build()
            chain.proceed(requestWithHeader)
        }

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        return OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(loggingInterceptor)
            .build()
    }


    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }


    @Provides
    @Singleton
    fun provideShelterluvApi(okHttpClient: OkHttpClient, json: Json): ShelterluvApi {
        return Retrofit.Builder()
            .baseUrl("https://new.shelterluv.com/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(ShelterluvApi::class.java)
    }

}