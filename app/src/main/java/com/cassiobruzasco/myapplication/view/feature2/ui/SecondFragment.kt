package com.cassiobruzasco.myapplication.view.feature2.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.cassiobruzasco.myapplication.R
import com.cassiobruzasco.myapplication.data.remote.repository.WeatherState
import com.cassiobruzasco.myapplication.databinding.FragmentSecondBinding
import com.cassiobruzasco.myapplication.view.feature2.viewmodel.SecondViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SecondFragment : Fragment() {

    private lateinit var binding: FragmentSecondBinding
    private val viewModel by viewModels<SecondViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initObs()
    }

    private fun initObs() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.weather.collect { state ->
                    when (state) {
                        WeatherState.Loading -> binding.loading.visibility = View.VISIBLE

                        is WeatherState.Success -> {
                            state.weatherItem.list.forEach {
                                binding.apply {
                                    loading.visibility = View.GONE
                                    text.visibility = View.VISIBLE
                                    text.text = resources.getString(
                                        R.string.weather_cast,
                                        it.temperature.day.toString(),
                                        it.humidity.toString()
                                    )
                                }
                            }
                        }
                        is WeatherState.Error -> {
                            Toast.makeText(requireContext(), state.errorMsg, Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        }
    }
}