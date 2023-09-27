package com.teamx.mariaFoods.ui.fragments.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.MainApplication
import com.teamx.mariaFoods.data.dataclasses.notificationModel.MainOrderList
import com.teamx.mariaFoods.databinding.ItemOrderBinding

class OrderListAdapter(
    private val orderProductArrayList: MainOrderList,
    private val onTopCategoriesListener: OnOrderListener
) : RecyclerView.Adapter<OrderListAdapter.OrdersViewHolder>() {

    lateinit var orderProductListAdapter: OrderProductListAdapter

    init {
        Log.d("TAG", "OnViewCreated123123333:${orderProductArrayList.jariis.size} ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }


    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {


        val ord = orderProductArrayList.jariis[position].productorderlist
//        ord.get(0).created_at = orderProductArrayList.jariis[position].created_at
//        val orderList: Jari1 = orderProductArrayList[position]


        holder.binding.orderProducts
        holder.binding.total.text = orderProductArrayList.jariis[position].total


//
//        notiArrayList.addAll(noti.item.jaris)

        val linearLayoutManager = LinearLayoutManager(MainApplication.context, RecyclerView.VERTICAL, false)
        holder.binding.orderProducts.layoutManager = linearLayoutManager

        Log.d("TAG", "OnViewCreated123123333@@:${ord.size} ")
        Log.d("TAG", "OnViewCreated123123333@@:${orderProductArrayList.jariis.get(position).id} ")
        orderProductListAdapter = OrderProductListAdapter(ord, onTopCategoriesListener)

        holder.binding.orderProducts.adapter = orderProductListAdapter


//        val orderProductList: Jari1 = orderProductArrayList[position]

//        holder.bind.productName.text = try {
//            orderList.name
//        } catch (e: Exception) {
//            ""
//        }
//
//        holder.bind.date.text = try {
//            orderList.created_at?.dropLast(18)
//        } catch (e: Exception) {
//            ""
//        }
//
//        holder.bind.ProductQuantity.text = try {
//            "Qty: ${orderList.quantity}"
//        } catch (e: Exception) {
//            ""
//        }
//
//        holder.bind.orderId.text = try {
//            "OrderId: ${orderList.orderId}"
//        } catch (e: Exception) {
//            ""
//        }
//        holder.bind.ProductPrice.text = try {
//            orderList.price
//        } catch (e: Exception) {
//            ""
//        }
//        holder.bind.orderStatus.text = try {
//            orderList.delivery_status
//        } catch (e: Exception) {
//            ""
//        }

//        if (orderList.delivery_status == "order-placed") {
//            holder.bind.btnCnclOrder.visibility = View.VISIBLE
//        }
//        else{
//            holder.bind.btnCnclOrderFalse.visibility = View.VISIBLE
//
//        }

        holder.bind.btnReOrder.setOnClickListener {
            onTopCategoriesListener.oneReorderClick(position)
        }


    }

    override fun getItemCount(): Int {
        return orderProductArrayList.jariis.size
    }


    class OrdersViewHolder(var binding: ItemOrderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val bind = binding

    }

}