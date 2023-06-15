package com.teamx.mariaFoods.ui.fragments.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.notification.Data
import com.teamx.mariaFoods.databinding.ItemNotificationBinding

class NotificationAdapter(
    private val notificationArrayList: ArrayList<Data>
) : RecyclerView.Adapter<NotificationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {


        val notificationList: Data = notificationArrayList[position]

        holder.bind.textView54.text =try {
            notificationList.Jun[0].title.toString()
        } catch (e: Exception) {
            ""
        }
        holder.bind.textView55.text =try {
            notificationList.Jun[0].body.toString()
        } catch (e: Exception) {
            ""
        }

        holder.bind.textView56.text =try {
            notificationList.Jun[0].time
        } catch (e: Exception) {
            ""
        }



    }

    override fun getItemCount(): Int {
        return notificationArrayList.size
    }
}

class NotificationViewHolder(private var binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}