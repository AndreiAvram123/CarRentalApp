package com.andrei.UI.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.databinding.SuggestionItemCarBinding

class SuggestionsAdapter : RecyclerView.Adapter<SuggestionsAdapter.ViewHolder>() {
   private val items = ArrayList<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SuggestionItemCarBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int =
       items.size

    fun setData(list:List<String>) {
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun clearData(){
        items.clear()
        notifyDataSetChanged()
    }

    inner class ViewHolder(private val binding:SuggestionItemCarBinding) :RecyclerView.ViewHolder (binding.root){

        fun bind(suggestion:String){
            binding.tvDeal.text = suggestion
         }
    }

}