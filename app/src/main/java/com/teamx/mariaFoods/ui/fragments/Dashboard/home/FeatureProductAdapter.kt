package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.databinding.ItemFeatureProductBinding

class FeatureProductAdapter(private val arrayList : ArrayList<Int>,private  val  onFeatureProductListener: OnFeatureProductListener):RecyclerView.Adapter<FeatureProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeatureProductViewHolder {
       val inflater = LayoutInflater.from(parent.context)
        val itemFeatureProductBinding = ItemFeatureProductBinding.inflate(inflater,parent,false)
        return FeatureProductViewHolder(itemFeatureProductBinding)
    }

    override fun onBindViewHolder(holder: FeatureProductViewHolder, position: Int) {

        holder.binding.imageView.setImageResource(arrayList[position])

        holder.itemView.setOnClickListener {

            onFeatureProductListener.OnFeatureProductClickListener(position)

        }
    }

    override fun getItemCount(): Int {


        return arrayList.size


    }


}

class FeatureProductViewHolder(itemFeatureProductBinding: ItemFeatureProductBinding): RecyclerView.ViewHolder(itemFeatureProductBinding.root){

    val binding = itemFeatureProductBinding
}

