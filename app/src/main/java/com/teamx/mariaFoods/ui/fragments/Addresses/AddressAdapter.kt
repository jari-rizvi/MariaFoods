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


        val addressList: Data? = addressArrayList[position]

        holder.bind.txtdeliveryAddressHouse.text =try {
            "P.O Box, ${addressList?.address_1}"
        } catch (e: Exception) {
            ""
        }


        holder.bind.postal.text =try {
            "Postal code ${addressList?.postal}"
        } catch (e: Exception) {
            ""
        }


        holder.bind.addressDelete.setOnClickListener {
            addressList?.id?.let { it1 -> onAddressListener.ondeleteClick(it1) }
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