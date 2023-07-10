package com.cassiobruzasco.myapplication.data.remote.repository

import com.cassiobruzasco.myapplication.data.remote.model.WeatherResponseItem
import com.cassiobruzasco.myapplication.data.remote.service.WeatherApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface WeatherRepository {
    suspend fun getWeather(location: String, count: Int): Flow<WeatherState>
}

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository {

    override suspend fun getWeather(location: String, count: Int) = flow {
        val response = api.getWeather(location = location, count = count)
        if (response.isSuccessful) {
            response.body()?.let { weatherItem ->
                emit(WeatherState.Success(weatherItem))
            } ?: run {
                emit(WeatherState.Error("body empty"))
            }
        } else {
            emit(WeatherState.Error("response error"))
        }
    }

    /**
     * here we use flow because its in the Google guideline
     * We --CAN'T-- use LiveData inside the repository
     * cost is heavy and we going to have side effects at the UI
     *
     * if you have a project that uses livedata follow the guideline below:
     * If you need to use streams of data in other layers of your app,
     * consider using Kotlin Flows and then converting them to LiveData in the ViewModel using asLiveData().
     * Learn more about using Kotlin Flow with LiveData in this code lab.
     * For code bases built with Java, consider using Executors in conjunction with callbacks or RxJava.
     *
     * https://developer.android.com/topic/libraries/architecture/livedata
     */
}

/**
 * this is just an example of state, in a production app try using a sealed class just for retrofit response
 * or any other data processing state
 *
 * I like to create a class called ResponseData that accepts any kind of object in the success state.
 * Check ResponseData file
 */
sealed class WeatherState {
    object Loading : WeatherState()
    class Success(val weatherItem: WeatherResponseItem) : WeatherState()
    class Error(val errorMsg: String) : WeatherState()
}