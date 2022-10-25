package com.cassiobruzasco.myapplication.data.remote.model

import com.google.gson.annotations.SerializedName

data class WeatherResponseItem(
    @SerializedName("list") val list: MutableList<DayModel>
)

data class DayModel(
    @SerializedName("temp") val temperature: TemperatureModel,
    @SerializedName("humidity") val humidity: Int,
)

data class TemperatureModel(
    @SerializedName("day") val day: Double
)