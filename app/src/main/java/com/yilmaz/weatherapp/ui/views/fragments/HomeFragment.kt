package com.yilmaz.weatherapp.ui.views.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.core.app.ActivityCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.google.android.gms.location.LocationServices
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.search.SearchBar
import com.google.android.material.search.SearchView
import com.google.android.material.textfield.TextInputLayout
import com.yilmaz.weatherapp.R
import com.yilmaz.weatherapp.data.local.database.PlacesModel
import com.yilmaz.weatherapp.databinding.FragmentHomeBinding
import com.yilmaz.weatherapp.ui.adapters.DailyWeatherAdapter
import com.yilmaz.weatherapp.ui.adapters.HourlyWeatherAdapter
import com.yilmaz.weatherapp.ui.adapters.PlacesAdapter
import com.yilmaz.weatherapp.ui.view_models.OpenWeatherViewModel
import com.yilmaz.weatherapp.ui.view_models.PlacesViewModel
import com.yilmaz.weatherapp.ui.view_models.SharedPreferencesViewModel
import com.yilmaz.weatherapp.ui.view_models.WeatherApiViewModel
import com.yilmaz.weatherapp.utils.Constants
import com.yilmaz.weatherapp.utils.DateHelper
import com.yilmaz.weatherapp.utils.LocationHelper
import com.yilmaz.weatherapp.utils.NetworkHelper
import com.yilmaz.weatherapp.utils.ShareHelper
import com.yilmaz.weatherapp.utils.SnackbarHelper
import dagger.hilt.android.AndroidEntryPoint
import java.time.LocalDateTime
import java.util.Locale


@Suppress("DEPRECATION")
@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home) {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val openWeatherViewModel: OpenWeatherViewModel by viewModels()
    private val weatherApiViewModel: WeatherApiViewModel by viewModels()
    private val placesViewModel: PlacesViewModel by viewModels()
    private val sharedPreferencesViewModel: SharedPreferencesViewModel by viewModels()

    private lateinit var hourlyWeatherAdapter: HourlyWeatherAdapter
    private lateinit var dailyWeatherAdapter: DailyWeatherAdapter
    private lateinit var placesAdapter: PlacesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requireActivity().window?.clearFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setUpHomeScreen()
        action()
        search()
    }

    override fun onStart() {
        super.onStart()
        setUpHomeScreen()
    }

    @SuppressLint("RtlHardcoded")
    private fun search() {
        binding.navigationViewEnd.getHeaderView(0)
            .findViewById<SearchView>(R.id.searchView).editText.addTextChangedListener { // query ->
                // if (query.toString() != "") getRetrofitResponse(query.toString())
            }

        binding.navigationViewEnd.getHeaderView(0)
            .findViewById<SearchView>(R.id.searchView).editText.setOnEditorActionListener { v, _, _ ->
                val cityName = v!!.text.toString()
                getRetrofitResponse(cityName)
                binding.drawerLayout.closeDrawer(Gravity.RIGHT)
                false
            }
    }

    @SuppressLint("RtlHardcoded")
    private fun action() {
        binding.navigationViewEnd.getHeaderView(0)
            .findViewById<SearchBar>(R.id.searchBar).setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.addNewPlaceMenu -> savePlace()
                }
                true
            }

        binding.searchButton.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.RIGHT)
        }

        binding.navigationButton.setOnClickListener {
            binding.drawerLayout.openDrawer(Gravity.LEFT)
        }

        binding.navigationViewStart.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigationDrawerSettings -> findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                R.id.navigationDrawerShare -> ShareHelper().shareWeather(
                    binding.locationTv.text.toString() +
                            "'s current weather is " +
                            binding.currentConditionDegreeTv.text.toString() +
                            "C°, " + binding.currentConditionTv.text.toString(),
                    requireContext()
                )
            }
            true
        }

    }

    private fun setUpHomeScreen() {
        if (NetworkHelper().isConnected(requireContext())) {
            getWeather()
            getSavedPlaces()
        } else {
            binding.homeScreenContainer.visibility = View.GONE
            binding.unconnectedIv.visibility = View.VISIBLE
        }
    }

    private fun getWeather() {
        if (checkPermission())
            if (LocationHelper().isLocationEnabled(requireActivity())) {
                getCityNameFromLocation()

            } else {
                MaterialAlertDialogBuilder(
                    requireContext(),
                    com.google.android.material.R.style.MaterialAlertDialog_Material3
                )
                    .setTitle("Warning")
                    .setMessage("Please enable location to get current location.")
                    .setPositiveButton("Enable") { _, _ ->
                        Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).also {
                            requireContext().startActivity(it)
                        }
                    }
                    .setNegativeButton("Dismiss") { _, _ ->
                        getRetrofitResponse(sharedPreferencesViewModel.getLastKnownCityName())
                    }
                    .setCancelable(false)
                    .show()
            }
        else
            requestPermission()
    }

    @SuppressLint("MissingPermission")
    private fun getCityNameFromLocation() {
        LocationServices.getFusedLocationProviderClient(requireActivity())
            .also { fusedLocationClient ->
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { location: Location? ->
                        val cityName = location?.let {
                            LocationHelper().getCityName(
                                it.latitude,
                                location.longitude,
                                requireContext()
                            )
                        }
                        if (cityName != null && cityName != "") {
                            getRetrofitResponse(cityName.toString())
                            sharedPreferencesViewModel.setLastKnownCityName(cityName)

                        } else {
                            getRetrofitResponse(sharedPreferencesViewModel.getLastKnownCityName())
                        }
                    }
            }
    }

    private fun getSavedPlaces() {
        placesAdapter = PlacesAdapter()

        binding.navigationViewEnd.getHeaderView(0).findViewById<RecyclerView>(R.id.placesRv).apply {
            setHasFixedSize(true)
            adapter = placesAdapter
            layoutManager = LinearLayoutManager(requireContext())
        }
        placesViewModel.getAllPlaces.observe(viewLifecycleOwner) { list ->
            placesAdapter.differ.submitList(list)
            deletePlace(list)

        }
        placesAdapter.setListener(object : PlacesAdapter.OnItemClickListener {
            @SuppressLint("RtlHardcoded")
            override fun onItemClick(model: PlacesModel?) {
                if (model != null) {
                    getRetrofitResponse(model.placeName!!)
                    sharedPreferencesViewModel.setLastKnownCityName(model.placeName)
                    binding.drawerLayout.closeDrawer(Gravity.RIGHT)
                }
            }
        })
    }

    private fun deletePlace(list: List<PlacesModel>) {
        ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                try {
                    val model: PlacesModel = list[viewHolder.adapterPosition]
                    placesViewModel.deletePlace(model)
                } catch (e: Exception) {
                    println(e.message.toString())
                }
            }
        }).attachToRecyclerView(
            binding.navigationViewEnd.getHeaderView(0).findViewById(R.id.placesRv)
        )
    }

    private fun savePlace() {
        val dialogView = this.layoutInflater.inflate(R.layout.alert_new_place, null)
        val subjectTextInputLayout = dialogView.findViewById<TextInputLayout>(R.id.nameTextField)

        MaterialAlertDialogBuilder(
            requireContext(),
            com.google.android.material.R.style.MaterialAlertDialog_Material3
        )
            .setTitle("Save a city")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                val placeName = subjectTextInputLayout.editText!!.text.toString()
                    .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                if (placeName.isNotEmpty())
                    placesViewModel.insertPlace(PlacesModel(null, placeName))
                else
                    SnackbarHelper().snackbar(requireView(), "Please enter a city name")
            }
            .show()

    }

    @SuppressLint("SetTextI18n")
    private fun getRetrofitResponse(city: String) {
        hourlyWeatherAdapter = HourlyWeatherAdapter()
        dailyWeatherAdapter = DailyWeatherAdapter()

        weatherApiViewModel.getHourlyResponse(Constants.WEATHER_API_KEY, city)
        weatherApiViewModel.responseHourly.observe(
            requireActivity()
        ) { result ->
            binding.locationTv.text = result.location.name + ", " + result.location.country
            binding.localTimeTv.text =
                "Local time: " + DateHelper().format(result.location.localtime) + ", " + "Last update: " + DateHelper().format(
                    result.current.last_updated
                )

            binding.currentConditionTv.text = result.current.condition.text
            binding.currentConditionIv.load("https:" + result.current.condition.icon)

            binding.currentConditionFeelsDegreeTv.text =
                "Feels like " + result.current.feelslike_c + "°"
            binding.currentConditionDegreeTv.text = result.current.temp_c.toString() + "°"

            binding.humidityTv.text = "% " + result.current.humidity
            binding.windSpeedTv.text = result.current.wind_kph.toString() + " kp/h"
            binding.pressureTv.text = result.current.pressure_mb.toString() + " hPa"

            binding.hourlyRv.apply {
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                setHasFixedSize(true)
                adapter = hourlyWeatherAdapter
                scrollToPosition(LocalDateTime.now().hour)
            }
            hourlyWeatherAdapter.differ.submitList(result.forecast.forecastday[0].hour)
        }

        openWeatherViewModel.getDailyResponse(city, Constants.OPEN_WEATHER_MAP_KEY)
        openWeatherViewModel.responseDaily.observe(
            requireActivity()
        ) { result ->
            binding.dailyRv.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = dailyWeatherAdapter
                setHasFixedSize(true)
            }
            dailyWeatherAdapter.differ.submitList(result.list)
        }
    }


    private fun checkPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(
            requireContext(),
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        requestPermissions(
            arrayOf(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION_PERMISSION_CODE
        )
    }

    @Suppress("OVERRIDE_DEPRECATION")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        )
            getWeather()
        else if (requestCode == LOCATION_PERMISSION_CODE &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_DENIED
        ) {
            val cityName = sharedPreferencesViewModel.getLastKnownCityName()
            getRetrofitResponse(cityName)
            SnackbarHelper().snackbar(
                requireView(),
                "Permission denied, default location $cityName"
            )
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val LOCATION_PERMISSION_CODE = 1
    }

}