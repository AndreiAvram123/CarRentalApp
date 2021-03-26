package com.andrei.UI.adapters.vouchers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.andrei.carrental.R
import com.andrei.carrental.databinding.RedeemVoucherLayoutBinding
import com.andrei.carrental.entities.Voucher

class VouchersAdapter(private val callback: VouchersAdapterCallback) : RecyclerView.Adapter<VouchersAdapter.VoucherViewHolder>() {
    private val vouchers:MutableList<Voucher> = mutableListOf()

    fun setData(data:List<Voucher>){
        vouchers.clear()
        vouchers.addAll(data)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VoucherViewHolder {
        val binding = RedeemVoucherLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return VoucherViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VoucherViewHolder, position: Int) {
        holder.bind(vouchers[position])
    }

    override fun getItemCount(): Int =vouchers.size



    inner class VoucherViewHolder(private val binding:RedeemVoucherLayoutBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(voucher:Voucher){
            binding.voucher = voucher
            binding.btRedeem.setOnClickListener {
                callback.onVoucherRedeemed()
            }
        }
    }
    interface VouchersAdapterCallback{
        fun onVoucherRedeemed()
    }


}