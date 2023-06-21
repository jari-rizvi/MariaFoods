package com.teamx.mariaFoods.ui.fragments.Checkout

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.databinding.ItemCheckoutBinding


class CartAdapter(
    val arrayList: ArrayList<Cart>
) : RecyclerView.Adapter<CartAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemCheckoutBinding = ItemCheckoutBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemCheckoutBinding)

    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {


        val cart: Cart = arrayList[position]

        holder.binding.productName.text = cart.product.name

        holder.binding.ProductPrice.text = "${cart.product.max_price}"

        holder.binding.ProductQuantity.text = "Qty: ${cart.qty}"

        Picasso.get().load(cart.product.product_images[0]).into(holder.binding.imageView12)

        holder.itemView.setOnClickListener {}


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemCheckoutBinding: ItemCheckoutBinding) :
        RecyclerView.ViewHolder(itemCheckoutBinding.root) {
        val binding = itemCheckoutBinding

    }
}