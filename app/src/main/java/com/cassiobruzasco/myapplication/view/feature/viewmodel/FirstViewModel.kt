package com.cassiobruzasco.myapplication.view.feature.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlin.random.Random

@ViewModelScoped
class FirstViewModel : ViewModel() {

    private val _roll = MutableStateFlow<RollState>(RollState.WaitingToRoll)
    val roll = _roll.asStateFlow()

    /**
     *  LiveData example - Google recommendations don't mention livedata anymore and
     *  is strongly recommended to build your apps with kotlin flows
     *  so in my professional opinion, we have much more to gain using flows.
     *  But regardless the usage of them should be similar, we just don't use livedata anymore.
     *  It was created to solve some urgent android problems, now we have a better and more
     *  sophisticated solution
     */
    private val _rollLiveData = MutableLiveData<RollState>(RollState.WaitingToRoll)
    val rollLiveData: LiveData<RollState> = _rollLiveData

    init {
        zipExample()

        collectOnEachFlow()
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
            emit("ENTRY")
            delay(1000L)
            emit("MAIN COURSE")
            delay(100L)
            emit("DESSERT")
        }
        viewModelScope.launch {
            flow.onEach {
                Log.d("FirstViewModel", "FLOW_ON_EACH: WAITER DELIVERS $it")
            }
                .collectLatest { // change to collect to see the right behavior for this scenario
                    Log.d("FirstViewModel", "FLOW_ON_EACH: CUSTOMER EATING $it")
                    delay(200L)
                    Log.d("FirstViewModel", "FLOW_ON_EACH: CUSTOMER FINISHED EATING $it")
                }
        }
    }

    fun rollInUi() {
        var random = Random.nextInt(1, 7)
        _roll.value = RollState.Roll(random)

        random = Random.nextInt(1, 7)
        _rollLiveData.value = RollState.Roll(random)
    }

}

sealed class RollState {
    object WaitingToRoll : RollState()
    class Roll(val value: Int) : RollState()
}
