package com.teamx.mariaFoods.ui.fragments.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.data.dataclasses.wishList.Approduct
import com.teamx.mariaFoods.databinding.ItemFavouriteListBinding


class FavouriteAdapter(
    val arrayList: ArrayList<Approduct>
) : RecyclerView.Adapter<FavouriteAdapter.FavouriteListViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteListViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemFavouriteListBinding = ItemFavouriteListBinding.inflate(inflater, parent, false)
        return FavouriteListViewHolder(itemFavouriteListBinding)

    }

    override fun onBindViewHolder(holder: FavouriteListViewHolder, position: Int) {

        val list: Approduct = arrayList[position]

        holder.binding.productName.text = list.name

        holder.binding.desc.text = list.short_description

        holder.binding.price.text = list.max_price.toString()

        Picasso.get().load(list.product_images[0]).into(holder.binding.imageView12)

        holder.itemView.setOnClickListener {}


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class FavouriteListViewHolder(itemFavouriteListBinding: ItemFavouriteListBinding) :
        RecyclerView.ViewHolder(itemFavouriteListBinding.root) {
        val binding = itemFavouriteListBinding

    }
}