package com.cassiobruzasco.myapplication.view.feature2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cassiobruzasco.myapplication.data.remote.model.WeatherResponseItem
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherRepository
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SecondViewModel @Inject constructor(private val weatherRepository: WeatherRepository) :
    ViewModel() {


    private val _weather = MutableStateFlow<WeatherState>(WeatherState.Loading)
    val weather: StateFlow<WeatherState> = _weather

    init {
        viewModelScope.launch {
            getWeather()
        }
    }

    private suspend fun getWeather() {
        _weather.value = weatherRepository.getWeather("Campinas", 1).single()
    }
}