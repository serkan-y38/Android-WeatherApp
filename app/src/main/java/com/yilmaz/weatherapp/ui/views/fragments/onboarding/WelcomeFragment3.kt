package com.yilmaz.weatherapp.ui.views.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yilmaz.weatherapp.R
import com.yilmaz.weatherapp.databinding.FragmentWelcome3Binding
import com.yilmaz.weatherapp.ui.view_models.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WelcomeFragment3 : Fragment(R.layout.fragment_welcome3) {

    private var _binding: FragmentWelcome3Binding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWelcome3Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.iv.setOnClickListener {
            finishOnboarding()
        }

        binding.tv.setOnClickListener {
            finishOnboarding()
        }

    }

    private fun finishOnboarding() {
        sharedPreferencesViewModel.setOnBoardingFinished()
        findNavController().navigate(R.id.action_viewPagerFragment2_to_homeFragment)
    }

}