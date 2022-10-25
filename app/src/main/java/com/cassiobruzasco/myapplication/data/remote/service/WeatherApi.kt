package com.cassiobruzasco.myapplication.data.remote.service

import com.cassiobruzasco.myapplication.data.remote.model.WeatherResponseItem
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("daily")
    suspend fun getWeather(
        @Query("q") location: String,
        @Query("mode") mode: String = "json",
        @Query("units") units: String = "metric",
        @Query("cnt") count: Int,
        @Query("appid") apiKey: String = "648a3aac37935e5b45e09727df728ac2",
    ): Response<WeatherResponseItem>
}