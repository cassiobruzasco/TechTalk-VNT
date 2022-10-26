package com.cassiobruzasco.myapplication.view.feature.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.cassiobruzasco.myapplication.R
import com.cassiobruzasco.myapplication.databinding.FragmentFirstBinding
import com.cassiobruzasco.myapplication.view.feature.viewmodel.FirstViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FirstFragment : Fragment() {

    private lateinit var binding: FragmentFirstBinding
    private val viewModel by viewModels<FirstViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        startStateFlow()

        binding.apply {

            buttonRoll.setOnClickListener {
                viewModel.rollInUi()
            }

            buttonNext.setOnClickListener {
                findNavController().navigate(R.id.action_FirstFragment_to_SecondFragment)
            }
        }
    }

    private fun startStateFlow() {
        // Na View nunca usar o lifecycleScope.launch sem o repeat
        // O repeat informa que só vai rodar enquanto esse lifecycle viver
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.roll.collectLatest { result ->
                    when (result) {
                        FirstViewModel.RollState.WaitingToRoll -> Unit
                        is FirstViewModel.RollState.Roll -> {
                            when (result.value) {
                                1 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_one))
                                2 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_two))
                                3 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_three))
                                4 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_four))
                                5 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_five))
                                6 -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_six))
                                else -> binding.dice.setImageDrawable(ContextCompat.getDrawable(requireContext(), R.drawable.icn_none))
                            }
                        }
                    }
                }
            }
        }
    }
}