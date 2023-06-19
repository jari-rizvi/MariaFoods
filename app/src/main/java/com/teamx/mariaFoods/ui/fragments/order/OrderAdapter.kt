package com.teamx.mariaFoods.ui.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.data.dataclasses.notificationModel.DataExtented1
import com.teamx.mariaFoods.databinding.ItemOrderMonthsBinding

class OrderAdapter(
    private val orderArrayList: ArrayList<DataExtented1>
) : RecyclerView.Adapter<OrderViewHolder>() {

    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderMonthsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val noti: DataExtented1 = orderArrayList[position]

        val orderList: DataExtented1 = orderArrayList[position]

        holder.bind.textView54.text = try {
            noti.item.name
        } catch (e: Exception) {

            ""
        }

        holder.binding.listtorder



//
//        notiArrayList.addAll(noti.item.jaris)

        val linearLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        holder.binding.listtorder.layoutManager = linearLayoutManager


        orderListAdapter = OrderListAdapter(noti.item.jariis)

        holder.binding.listtorder.adapter = orderListAdapter


    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }
}

class OrderViewHolder(var binding: ItemOrderMonthsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}