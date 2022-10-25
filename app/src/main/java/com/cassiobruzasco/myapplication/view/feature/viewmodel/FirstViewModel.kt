package com.cassiobruzasco.myapplication.view.feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random

@ViewModelScoped
class FirstViewModel: ViewModel() {

    private val _roll = MutableStateFlow<RollState>(RollState.RollIdle)
    val roll = _roll

    init {
        collectFlow()
    }

    private val dice = flow {
        val random = Random.nextInt(0, 7)
        emit(random)
    }

    private fun collectFlow() {
        viewModelScope.launch {
            dice.collect { roll ->
               Log.d("FirstViewModel", "Rolled: $roll")
            }
        }
    }

    fun rollInUi() {
        val random = Random.nextInt(1, 7)
        _roll.update { RollState.RollSuccess(random) }
        // Outro modo de atribuir valor ao stateflow
        // _roll.value = RollState.RollSuccess(random)
    }

    sealed class RollState {
        object RollIdle: RollState()
        class RollSuccess(val value: Int): RollState()
    }
}