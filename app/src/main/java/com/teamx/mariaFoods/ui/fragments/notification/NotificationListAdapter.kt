package com.teamx.mariaFoods.ui.fragments.notification

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Jari
import com.teamx.mariaFoods.databinding.ItemNotificationBinding

class NotificationListAdapter(
    private val notificationsArrayList: List<Jari>
) : RecyclerView.Adapter<NotificationsViewHolder>() {
init {
    Log.d("TAG", "OnViewCreated123123333:${notificationsArrayList.size} ")
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        return NotificationsViewHolder(
            ItemNotificationBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {

        val notificationsList: Jari = notificationsArrayList[position]

        holder.bind.textView54.text = try {
            notificationsList.title
        } catch (e: Exception) {
            ""
        }

        holder.bind.textView55.text = try {
            notificationsList.body
        } catch (e: Exception) {
            ""
        }
        holder.bind.textView56.text = try {
            notificationsList.time
        } catch (e: Exception) {
            ""
        }

    }

    override fun getItemCount(): Int {
        return notificationsArrayList.size
    }
}

class NotificationsViewHolder(private var binding: ItemNotificationBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}