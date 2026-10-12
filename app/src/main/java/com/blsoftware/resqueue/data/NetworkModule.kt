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
import retrofit2.Retrofit
import javax.inject.Singleton
import kotlin.jvm.java
import retrofit2.converter.gson.GsonConverterFactory

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
                    "Bearer ${BuildConfig.SHELTERLUV_API_KEY}"
                ) // Replace "x-api-key" with key name required by your API docs
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
    fun provideShelterluvApi(okHttpClient: OkHttpClient): ShelterluvApi {
        return Retrofit.Builder()
            .baseUrl("https://new.shelterluv.com/api/v1/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ShelterluvApi::class.java)
    }

}