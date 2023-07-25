package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.products.TimeSlot
import com.teamx.mariaFoods.databinding.ItemTimeSlotsBinding


class TimeAdapter(
    val arrayList: ArrayList<TimeSlot>, val onTimeListener: OnTimeListener
) : RecyclerView.Adapter<TimeAdapter.TimeSlotViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTimeSlotBinding = ItemTimeSlotsBinding.inflate(inflater, parent, false)
        return TimeSlotViewHolder(itemTimeSlotBinding)

    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {

        val timeSlots: TimeSlot = arrayList[position]

        holder.binding.time.text = timeSlots.timeline

        holder.binding.textView15.text = timeSlots.name

        holder.binding.checkedTextView.isChecked = timeSlots.isChecked

        holder.itemView.setOnClickListener {
            onTimeListener.ontimeClick(position)
        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TimeSlotViewHolder(itemTimeSlotBinding: ItemTimeSlotsBinding) :
        RecyclerView.ViewHolder(itemTimeSlotBinding.root) {
        val binding = itemTimeSlotBinding

    }
}