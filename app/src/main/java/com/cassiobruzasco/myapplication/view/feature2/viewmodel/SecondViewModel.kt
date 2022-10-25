package com.cassiobruzasco.myapplication.view.feature2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassiobruzasco.myapplication.data.remote.model.WeatherResponseItem
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(private val weatherRepository: WeatherRepository): ViewModel() {


    private val _weather = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weather = _weather

    init {
        getWeather()
    }

    private fun getWeather() {
        viewModelScope.launch {
            val response = weatherRepository.getWeather("Campinas", 10)
            if (response.isSuccessful) {
                response.body()?.let { weatherItem ->
                    _weather.update { WeatherState.Success(weatherItem) }
                } ?: kotlin.run {
                    _weather.update { WeatherState.Error }
                }
            }
        }
    }

    sealed class WeatherState {
        object Loading: WeatherState()
        class Success(val weatherItem: WeatherResponseItem): WeatherState()
        object Error: WeatherState()
    }
}