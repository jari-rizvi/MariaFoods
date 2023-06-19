package com.teamx.mariaFoods.ui.fragments.order

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.notificationModel.Jari1
import com.teamx.mariaFoods.databinding.ItemOrderBinding

class OrderListAdapter(
    private val orderArrayList: List<Jari1>
) : RecyclerView.Adapter<NotificationsViewHolder>() {
init {
    Log.d("TAG", "OnViewCreated123123333:${orderArrayList.size} ")
}
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsViewHolder {
        return NotificationsViewHolder(
            ItemOrderBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: NotificationsViewHolder, position: Int) {

        val orderList: Jari1 = orderArrayList[position]

        holder.bind.productName.text = try {
            orderList.name
        } catch (e: Exception) {
            ""
        }

        holder.bind.date.text = try {
            orderList.created_at.dropLast(18)
        } catch (e: Exception) {
            ""
        }

        holder.bind.ProductQuantity.text = try {
            orderList.quantity
        } catch (e: Exception) {
            ""
        }
        holder.bind.ProductPrice.text = try {
            orderList.price
        } catch (e: Exception) {
            ""
        }

    }

    override fun getItemCount(): Int {
        return orderArrayList.size
    }
}

class NotificationsViewHolder(private var binding: ItemOrderBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}