package com.teamx.mariaFoods.ui.fragments.Addresses

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.getAddress.Data
import com.teamx.mariaFoods.databinding.ItemAddressBinding

class AddressAdapter(
    private val addressArrayList: ArrayList<Data>,
    private val onAddressListener: OnAddressListener
) : RecyclerView.Adapter<AddressViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemAddressBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {


        val addressList: Data = addressArrayList[position]

        holder.bind.txtdeliveryAddressHouse.text = "P.O Box, ${addressList.address_1}"
        holder.bind.postal.text = "Postal code ${addressList.postal}"



        holder.bind.addressDelete.setOnClickListener {
            onAddressListener.ondeleteClick(addressList.id)
        }
        holder.bind.addressEditIcon.setOnClickListener {
            onAddressListener.oneditClick(position)

        }

    }

    override fun getItemCount(): Int {
        return addressArrayList.size
    }
}

class AddressViewHolder(private var binding: ItemAddressBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}