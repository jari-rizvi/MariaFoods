package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.databinding.ItemFeatureProductBinding


class ProductBannersAdapter(
    val arrayList: ArrayList<String>
) : RecyclerView.Adapter<ProductBannersAdapter.TopProductViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemFeatureProductBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {
        val product: String = arrayList[position]

        Picasso.get().load(product).into(holder.binding.imageView)


    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemProductBannerBinding: ItemFeatureProductBinding) :
        RecyclerView.ViewHolder(itemProductBannerBinding.root) {
        val binding = itemProductBannerBinding

    }
}