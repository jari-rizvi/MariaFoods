package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.products.OrderDay
import com.teamx.mariaFoods.databinding.ItemDaysBinding


class DateAdapter(
    val arrayList: ArrayList<OrderDay>,val onTimeListener: OnTimeListener) : RecyclerView.Adapter<DateAdapter.DaysViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DaysViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemDaysBinding = ItemDaysBinding.inflate(inflater, parent, false)
        return DaysViewHolder(itemDaysBinding)

    }

    override fun onBindViewHolder(holder: DaysViewHolder, position: Int) {

        val days: OrderDay = arrayList[position]

        holder.binding.day.text = days.name

        holder.binding.date.text =  days.date


        holder.binding.checkedTextView.isChecked = days.isChecked

        holder.itemView.setOnClickListener {
            onTimeListener.ondaysClick(position)
        }




    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class DaysViewHolder(itemDaysBinding: ItemDaysBinding) :
        RecyclerView.ViewHolder(itemDaysBinding.root) {
        val binding = itemDaysBinding

    }
}