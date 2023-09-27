package com.teamx.mariaFoods.ui.fragments.cart

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.data.dataclasses.getCart.Cart
import com.teamx.mariaFoods.databinding.ItemCartBinding
import com.teamx.mariaFoods.ui.fragments.Dashboard.home.OnCartListener


class CarttAdapter(
    val arrayList: ArrayList<Cart>,
    val onCartListener: OnCartListener
) : RecyclerView.Adapter<CarttAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemCartBinding = ItemCartBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemCartBinding)

    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val cart = arrayList[position]

        holder.binding.productName.text = cart.product.name

        holder.binding.textView18.text = "${cart.product.max_price} AED"


        Picasso.get().load(cart.product.product_images[0]).into(holder.binding.imageView12)

        holder.itemView.setOnClickListener {}

        if ( cart.qty < 1) {
            cart.qty = 1
        }
        holder.binding.textView19.text = "${cart.qty}"

//        holder.binding.btnAdd.setOnClickListener {
//            onCartListener?.onAddClickListener(position)
//        }

//        holder.binding.btnSub.setOnClickListener {
//            onCartListener?.onSubClickListener(position)
//        }
        holder.binding.btnDelete.setOnClickListener {
            onCartListener?.onRemoveToCartListener(position)
        }

        holder.binding.btnAdd.setOnClickListener {
            onCartListener.onQuantityChange(position,cart.qty + 1)
//            onCartListener?.onAddClickListener(position)
        }

        holder.binding.btnSub.setOnClickListener {
            onCartListener.onQuantityChange(position,cart.qty - 1)
//            onCartListener?.onSubClickListener(position)
        }

//        holder.binding.btnBuy.setOnClickListener {
//            cart.variation!!.id?.let { it1 -> onCartListener?.onAddToCartListener(it1) }
//        }


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemCartBinding: ItemCartBinding) :
        RecyclerView.ViewHolder(itemCartBinding.root) {
        val binding = itemCartBinding

    }
}