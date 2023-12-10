package com.yilmaz.weatherapp.ui.views.fragments.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.yilmaz.weatherapp.R
import com.yilmaz.weatherapp.databinding.FragmentWelcome1Binding


class WelcomeFragment1 : Fragment(R.layout.fragment_welcome1) {

    private var _binding: FragmentWelcome1Binding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWelcome1Binding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)

        binding.iv.setOnClickListener {
            viewPager?.currentItem = 1
        }

        binding.tv.setOnClickListener {
            viewPager?.currentItem = 1

        }

    }

}