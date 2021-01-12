package com.andrei.UI.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.databinding.SuggestionItemCarBinding
import com.andrei.carrental.entities.CarSearchEntity

class SuggestionsAdapter : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
   private val items = ArrayList<CarSearchEntity>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestionItemCarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
       items.size

    fun setData(list:List<CarSearchEntity>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData(){
        items.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding:SuggestionItemCarBinding) :RecyclerView.ViewHolder (binding.root){

        fun bind(carSearchEntity: CarSearchEntity){
            binding.car = carSearchEntity

         }
    }

}