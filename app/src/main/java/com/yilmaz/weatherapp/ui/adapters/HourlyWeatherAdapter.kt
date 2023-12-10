package com.yilmaz.weatherapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yilmaz.weatherapp.databinding.ListItemHourBinding
import com.yilmaz.weatherapp.utils.DateHelper

class HourlyWeatherAdapter : RecyclerView.Adapter<HourlyWeatherAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ListItemHourBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallBack = object :
        DiffUtil.ItemCallback<com.yilmaz.weatherapp.data.remote.api.models.weather_api.Hour>() {

        override fun areItemsTheSame(
            oldItem: com.yilmaz.weatherapp.data.remote.api.models.weather_api.Hour,
            newItem: com.yilmaz.weatherapp.data.remote.api.models.weather_api.Hour
        ): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(
            oldItem: com.yilmaz.weatherapp.data.remote.api.models.weather_api.Hour,
            newItem: com.yilmaz.weatherapp.data.remote.api.models.weather_api.Hour
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)


    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ListItemHourBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = differ.currentList[position]

        holder.itemBinding.hourlyLIDateTv.text = DateHelper().format(model.time)
        holder.itemBinding.hourlyLITempTv.text = model.temp_c.toString() + "°"
        holder.itemBinding.hourlyListItemIv.load("https:" + model.condition.icon)

    }

}