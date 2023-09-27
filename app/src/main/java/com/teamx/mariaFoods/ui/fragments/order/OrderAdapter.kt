package com.teamx.mariaFoods.ui.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.data.dataclasses.notificationModel.MainDateOrderList
import com.teamx.mariaFoods.databinding.ItemOrderMonthsBinding

class OrderAdapter(
    private val orderArrayList: ArrayList<MainDateOrderList>,
    private val onOrderListener: OnOrderListener
) : RecyclerView.Adapter<OrderViewHolder>(), OnOrderListener {

    lateinit var orderListAdapter: OrderListAdapter

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderMonthsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {

        val noti: MainDateOrderList = orderArrayList[position]

        val orderList: MainDateOrderList = orderArrayList[position]

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

//        Log.d("TAG", "__OnViewCreated123123333:${noti.item.jariis.get(0).productorderlist.size} ")
        orderListAdapter = OrderListAdapter(noti.item,this)

        holder.binding.listtorder.adapter = orderListAdapter


    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }

    override fun oneReorderClick(position: Int) {
        onOrderListener.oneReorderClick(position)
    }

    override fun oneCancelOrderClick(position: Int) {
        onOrderListener.oneCancelOrderClick(position)
    }
}

class OrderViewHolder(var binding: ItemOrderMonthsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}