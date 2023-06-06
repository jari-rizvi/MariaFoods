package com.teamx.mariaFoods.ui.fragments.order

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.orderHistory.Data
import com.teamx.mariaFoods.databinding.ItemOrderBinding

class OrderListAdapter(
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

//        val orderList: DocX = orderArrayList[position]
//        holder.bind.orderId.text = "Order Id # " + orderList._id.dropLast(18)
//        holder.bind.orderQty.text = "Qty: " + orderList.products!![0].product_id.quantity.toString()
//        holder.bind.ProductName.text = orderList.products!![0].product_id.name.toString()
//        holder.bind.orderType.text = orderList.products!![0].product_id.type.dropLast(18)
//        holder.bind.orderPrice.text = orderList.products!![0].product_id.price.toString()
//        holder.bind.price.text = "AED "+ orderList.products!![0].product_id.price.toString()
//        holder.bind.pickupdate.text = orderList.delivery_time.toString()
//        holder.bind.note.text = orderList.delivery_time.toString()
//        Picasso.get().load(orderList.products!![0].product_id.image).into(holder.bind.img)

        val orderList: Data = orderArrayList[position]
//        holder.bind.productName.text = orderList.applied_coupon_code



//        holder.bind.btnReview.setOnClickListener {
//            onOrderListListener.onAddReviewClickListener(orderList._id)
//        }

//        holder.itemView.setOnClickListener {
//            onOrderListListener.onOrderClickListener(orderList._id)
//        }


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