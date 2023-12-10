package com.yilmaz.weatherapp.ui.views.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.yilmaz.weatherapp.R
import com.yilmaz.weatherapp.databinding.FragmentSettingsBinding
import com.yilmaz.weatherapp.ui.view_models.SharedPreferencesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingsFragment : Fragment(R.layout.fragment_settings) {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.themeSwitch.isChecked = sharedPreferencesViewModel.getTheme()

        action()
    }

    private fun action() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                findNavController().navigate(R.id.action_settingsFragment_to_homeFragment)
            }

            themeSwitch.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPreferencesViewModel.setTheme(true)

                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPreferencesViewModel.setTheme(false)
                }

            }

        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}