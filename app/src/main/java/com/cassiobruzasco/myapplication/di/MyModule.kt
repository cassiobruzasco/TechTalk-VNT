package com.cassiobruzasco.myapplication.di

import android.content.Context
import androidx.annotation.StringRes
import com.cassiobruzasco.myapplication.MyApplication
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherRepository
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherRepositoryImpl
import com.cassiobruzasco.myapplication.data.remote.service.WeatherApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object MyModule {

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/forecast/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApplication(@ApplicationContext app: Context): MyApplication{
        return app as MyApplication
    }

    @Singleton
    @Provides
    fun provideServiceApi(retrofit: Retrofit): WeatherApi {
        return retrofit.create(WeatherApi::class.java)
    }

    @Singleton
    @Provides
    fun provideWeatherRepository(api: WeatherApi): WeatherRepository {
        return WeatherRepositoryImpl(api)
    }
}