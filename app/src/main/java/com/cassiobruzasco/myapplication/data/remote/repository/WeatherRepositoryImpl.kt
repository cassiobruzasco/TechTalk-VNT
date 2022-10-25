package com.cassiobruzasco.myapplication.data.remote.repository

import com.cassiobruzasco.myapplication.data.remote.model.WeatherResponseItem
import com.cassiobruzasco.myapplication.data.remote.service.WeatherApi
import retrofit2.Response
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(private val api: WeatherApi): WeatherRepository {

    override suspend fun getWeather(location: String, count: Int): Response<WeatherResponseItem> {
        return api.getWeather(location = location, count = count)
    }
}