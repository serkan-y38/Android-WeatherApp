package com.yilmaz.weatherapp.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.yilmaz.weatherapp.data.local.database.PlacesModel
import com.yilmaz.weatherapp.databinding.ListItemPlacesBinding

class PlacesAdapter : RecyclerView.Adapter<PlacesAdapter.ViewHolder>() {

    private lateinit var listener: OnItemClickListener

    inner class ViewHolder(val binding: ListItemPlacesBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val differCallBack = object : DiffUtil.ItemCallback<PlacesModel>() {

        override fun areItemsTheSame(oldItem: PlacesModel, newItem: PlacesModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: PlacesModel, newItem: PlacesModel): Boolean {
            return oldItem == newItem
        }

    }
    val differ = AsyncListDiffer(this, differCallBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            ListItemPlacesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val model = differ.currentList[position]

        holder.binding.apply {
            placeItemTV.text = model.placeName

            placesListItemMainCV.setOnClickListener {
                listener.onItemClick(model)
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClick(model: PlacesModel?)
    }

    fun setListener(listener: OnItemClickListener) {
        this.listener = listener
    }

}