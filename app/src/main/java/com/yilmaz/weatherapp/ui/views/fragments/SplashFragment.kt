@file:Suppress("DEPRECATION")

package com.yilmaz.weatherapp.ui.views.fragments

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yilmaz.weatherapp.R
import com.yilmaz.weatherapp.databinding.FragmentSplashBinding
import com.yilmaz.weatherapp.ui.view_models.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment : Fragment(R.layout.fragment_splash) {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (sharedPreferencesViewModel.getTheme())
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        else
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        requireActivity().window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        Handler().postDelayed({
            view.post {
                if (sharedPreferencesViewModel.isOnBoardingFinished())
                    findNavController().navigate(R.id.action_splashFragment2_to_homeFragment)
                else
                    findNavController().navigate(R.id.action_splashFragment2_to_viewPagerFragment2)
            }

        }, 1000)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}