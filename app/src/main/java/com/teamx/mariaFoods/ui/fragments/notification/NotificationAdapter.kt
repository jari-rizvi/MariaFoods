package com.teamx.mariaFoods.ui.fragments.notification

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.data.dataclasses.notificationModel.DataExtented
import com.teamx.mariaFoods.databinding.ItemNotificationMonthsBinding

class NotificationAdapter(
    private val notificationArrayList: ArrayList<DataExtented>
) : RecyclerView.Adapter<NotificationViewHolder>() {

    lateinit var notificationListAdapter: NotificationListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationMonthsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {

        val noti: DataExtented = notificationArrayList[position]

        val notificationList: DataExtented = notificationArrayList[position]

        holder.bind.textView54.text = try {
            noti.item.name
        } catch (e: Exception) {

            ""
        }

        holder.binding.listtnotification



//
//        notiArrayList.addAll(noti.item.jaris)

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.binding.listtnotification.layoutManager = linearLayoutManager


        notificationListAdapter = NotificationListAdapter(noti.item.jaris)

        holder.binding.listtnotification.adapter = notificationListAdapter


    }

    override fun getItemCount(): Int {
        return notificationArrayList.size
    }
}

class NotificationViewHolder(var binding: ItemNotificationMonthsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}