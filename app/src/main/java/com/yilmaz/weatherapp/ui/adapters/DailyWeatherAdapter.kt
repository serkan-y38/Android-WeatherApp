package com.yilmaz.weatherapp.ui.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.yilmaz.weatherapp.databinding.ListItemDailyWeatherBinding

class DailyWeatherAdapter : RecyclerView.Adapter<DailyWeatherAdapter.ViewHolder>() {

    class ViewHolder(val itemBinding: ListItemDailyWeatherBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

    private val differCallBack = object :
        DiffUtil.ItemCallback<com.yilmaz.weatherapp.data.remote.api.models.open_w_map.item>() {

        override fun areItemsTheSame(
            oldItem: com.yilmaz.weatherapp.data.remote.api.models.open_w_map.item,
            newItem: com.yilmaz.weatherapp.data.remote.api.models.open_w_map.item
        ): Boolean {
            return oldItem.dt == newItem.dt
        }

        override fun areContentsTheSame(
            oldItem: com.yilmaz.weatherapp.data.remote.api.models.open_w_map.item,
            newItem: com.yilmaz.weatherapp.data.remote.api.models.open_w_map.item
        ): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ListItemDailyWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = differ.currentList[position]
        val degreeK = model.main.temp
        val degreeC = (degreeK - 273.15).toInt()

        holder.itemBinding.apply {
            dailyDateListView.text = model.dt_txt
            dailyTempListView.text = "$degreeC°"
            dailyImageListView.load("https://openweathermap.org/img/wn/" + model.weather[0].icon + "@2x.png")
        }
    }
}