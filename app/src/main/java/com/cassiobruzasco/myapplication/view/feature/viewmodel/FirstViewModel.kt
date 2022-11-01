package com.cassiobruzasco.myapplication.view.feature.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlin.random.Random

@ViewModelScoped
class FirstViewModel: ViewModel() {

    private val _roll = MutableStateFlow<RollState>(RollState.WaitingToRoll)
    val roll: StateFlow<RollState> = _roll

    init {
        collectFlow()

        zipExample()

        collectOnEachFlow()
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

    private fun zipExample() {
        viewModelScope.launch {
            val flowInt = flowOf(1, 2, 3)
            val flowString = flowOf("A", "B", "C")
            flowInt.zip(flowString) { intValue, stringValue ->
                "$intValue$stringValue"
            }.collect {
                Log.d("FirstViewModel", it)
            }
        }
    }

    private fun collectOnEachFlow() {
        val flow = flow {
            delay(150L)
            emit("ENTRADA")
            delay(1000L)
            emit("PRATO PRINCIPAL")
            delay(100L)
            emit("SOBREMESA")
        }
        viewModelScope.launch {
            flow.onEach {
                Log.d("FirstViewModel", "FLOWONEACH: $it entregue")
            }
                //Se quiser que o collect rode em uma courotine separada, adicione um buffer
                //.buffer
                .collectLatest { // trocar pro collect para ver o funcionamento certo
                    Log.d("FirstViewModel", "FLOWONEACH: comendo $it")
                    delay(200L)
                    Log.d("FirstViewModel", "FLOWONEACH: terminou de comer $it")
                }
        }
    }

    fun rollInUi() {
        val random = Random.nextInt(1, 7)
        _roll.update { RollState.Roll(random) }

        // Outro modo de atribuir valor ao stateflow
        // _roll.value = RollState.RollSuccess(random)
    }

    sealed class RollState {
        object WaitingToRoll: RollState()
        class Roll(val value: Int): RollState()
    }
}