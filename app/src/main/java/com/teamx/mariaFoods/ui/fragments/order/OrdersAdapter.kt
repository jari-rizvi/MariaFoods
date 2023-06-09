package com.teamx.mariaFoods.ui.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.orderHistory.Data
import com.teamx.mariaFoods.databinding.ItemOrderBinding

class OrderAdapter(
    private val orderArrayList: ArrayList<Data>,
    private val onOrderListener: OnOrderListener
) : RecyclerView.Adapter<OrderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderViewHolder {
        return OrderViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: OrderViewHolder, position: Int) {


        val orderList: Data = orderArrayList[position]

        holder.bind.productName.text =try {
            orderList.product.name
        } catch (e: Exception) {
            ""
        }
        holder.bind.ProductQuantity.text =try {
            orderList.order_quantity.toString()
        } catch (e: Exception) {
            ""
        }

        holder.bind.ProductPrice.text =try {
            orderList.product.max_price.toString()
        } catch (e: Exception) {
            ""
        }

        holder.bind.date.text =try {
            orderList.created_at.dropLast(17)
        } catch (e: Exception) {
            ""
        }

        holder.bind.btnReOrder.setOnClickListener {
            onOrderListener.oneOrderClick(position)
        }



    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }
}

class OrderViewHolder(private var binding: ItemOrderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}