package com.di

import android.content.Context
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.custom.ResponseHandler
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.kagroup.baseProject.R
import com.kagroup.baseProject.core.source.local.UserDataSource
import com.kagroup.baseProject.core.source.remote.WebService
import com.utils.Constants
import com.utils.Domain
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.CallAdapter
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by karim on 21,November,2020
 */
@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideWebServices(okHttpClient:OkHttpClient,
                           converterFactory:GsonConverterFactory,
                           callAdapterFactory:CallAdapter.Factory): WebService {
        return Retrofit
            .Builder().apply {
                client(okHttpClient)
                addConverterFactory(converterFactory)
                baseUrl(Constants.BASE_URL)
                addCallAdapterFactory(callAdapterFactory)
            }.build()
            .create(WebService::class.java)
    }

    @Singleton
    @Provides
    fun provideGlideInstance(@ApplicationContext context: Context) =
        Glide.with(context).apply {
            setDefaultRequestOptions(
                RequestOptions()
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
            )
        }

    @Singleton
    @Provides
    fun provideResponseHandler() : ResponseHandler = ResponseHandler()

    @Singleton
    @Provides
    fun provideHTTPLoggingInterceptor(): HttpLoggingInterceptor {
        val interceptor = HttpLoggingInterceptor { message -> Timber.e(message) };
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Named("network_interceptor")
    @Singleton
    @Provides
    fun provideNetworkInterceptor(): Interceptor {

        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    request.addHeader("Authorization", UserDataSource.getToken())
                }
                job.join()
            }
            chain.proceed(request.build())
        }
    }

    @Named("language_interceptor")
    @Singleton
    @Provides
    fun provideLanguageInterceptor(): Interceptor {
        return Interceptor { chain ->
            val original = chain.request()
            val request = original.newBuilder()
            runBlocking {
                val job = CoroutineScope(Dispatchers.IO).launch {
                    request.addHeader("Accept-Language", "ar")
                    request.addHeader("Content-Type", Constants.CONTENT_TYPE)
                    request.addHeader("Accept", Constants.CONTENT_TYPE)
                }
                job.join()
            }
            chain.proceed(request.build())
        }
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(
        cache: Cache,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        @Named("network_interceptor")networkInterceptor: Interceptor,
        @Named("language_interceptor")languageInterceptor: Interceptor,
    ): OkHttpClient {
        return OkHttpClient().newBuilder().apply {
            connectTimeout(1, TimeUnit.MINUTES)
            readTimeout(1, TimeUnit.MINUTES)
            writeTimeout(1, TimeUnit.MINUTES)
            addInterceptor(languageInterceptor)
            addInterceptor(httpLoggingInterceptor)
            addNetworkInterceptor(networkInterceptor)
            cache(cache)
        }.build()

    }

    @Singleton
    @Provides
    fun provideJsonConverterFactory(gson: Gson): Converter.Factory {
        return GsonConverterFactory.create(gson);
    }

    @Singleton
    @Provides
    fun provideCallAdapter(): CallAdapter.Factory {
        return RxJava2CallAdapterFactory.create();
    }

    @Singleton
    @Provides
    fun provideCache(): Cache {
        return Cache(File(Domain.application.cacheDir,
            "Responses"), (10 * 1000 * 1000).toLong())
    }

    @Singleton
    @Provides
    fun provideGson() = GsonBuilder().create()

}