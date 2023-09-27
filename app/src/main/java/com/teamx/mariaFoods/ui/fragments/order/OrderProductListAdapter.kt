package com.teamx.mariaFoods.ui.fragments.order

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.notificationModel.ProductOrderList
import com.teamx.mariaFoods.databinding.ItemOrderProductsBinding

class OrderProductListAdapter(
    private val orderArrayList: List<ProductOrderList>,
    private val onTopCategoriesListener: OnOrderListener
) : RecyclerView.Adapter<OrderProductListAdapter.OrdersViewHolder>() {
    init {
        Log.d("TAG", "OnViewCreated123123333:${orderArrayList.size} ")
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersViewHolder {
        return OrdersViewHolder(
            ItemOrderProductsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: OrdersViewHolder, position: Int) {

        val orderList: ProductOrderList = orderArrayList[position]

        holder.bind.productName.text = try {
            orderList.name
        } catch (e: Exception) {
            ""
        }

        holder.bind.date.text = try {
           orderList.created_at
        } catch (e: Exception) {
            ""
        }

        holder.bind.ProductQuantity.text = try {
            "Qty: ${orderList.quantity}"
        } catch (e: Exception) {
            ""
        }

        holder.bind.ProductPrice.text = try {
            orderList.price
        } catch (e: Exception) {
            ""
        }
        holder.bind.orderStatus.text = try {
            orderList.delivery_status
        } catch (e: Exception) {
            ""
        }

//        if (orderList.delivery_status == "order-placed") {
//            holder.bind.btnCnclOrder.visibility = View.VISIBLE
//        }
//        else{
//            holder.bind.btnCnclOrderFalse.visibility = View.VISIBLE
//
//        }

       /* holder.bind.btnReOrder.setOnClickListener {
            onTopCategoriesListener.oneReorderClick(position)
        }*/

//        holder.bind.btnCnclOrder.setOnClickListener {
////            onTopCategoriesListener.oneCancelOrderClick(orderArrayList[position].id!!)
//        }

    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }


    class OrdersViewHolder(private var binding: ItemOrderProductsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        val bind = binding

    }

}