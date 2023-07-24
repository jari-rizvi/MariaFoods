package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.teamx.mariaFoods.data.dataclasses.products.Data
import com.teamx.mariaFoods.databinding.ItemProductsBinding


class ProductAdapter(
    val arrayList: ArrayList<Data>,
    private val onTopProductListener: OnProductListener,
    val onCartListener: OnCartListener
) : RecyclerView.Adapter<ProductAdapter.TopProductViewHolder>() {

    lateinit var ProductBannerAdapter: ProductBannersAdapter
    private var tabLayoutMediator: TabLayoutMediator? = null


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemProductsBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        val productBannner: Data = arrayList[position]

        val product: Data = arrayList[position]

        holder.binding.productName.text = product.name

        holder.binding.price.text = "${product.max_price} AED"

//       val quanti = product.qty ?: 1
        if ( product.qty < 1) {
            product.qty = 1
        }
        holder.binding.textView19.text = "${product.qty}"



        holder.binding.productDescription.text = product.short_description


        holder.binding.screenViewpager

        val productBannerArrayList: List<String?>? = productBannner.product_images

        val arrayList = ArrayList<String>()

        productBannerArrayList?.forEach {

            if (it != null) {
                arrayList.add(it)
            }
        }



        ProductBannerAdapter = ProductBannersAdapter(arrayList)
        holder.binding.screenViewpager.adapter = ProductBannerAdapter

        TabLayoutMediator(
            holder.binding.tabIndicator, holder.binding.screenViewpager
        ) { tab, position ->
            tab.text = productBannerArrayList!![position].toString()
        }.attach()


        tabLayoutMediator = TabLayoutMediator(
            holder.binding.tabIndicator, holder.binding.screenViewpager
        ) { tab: TabLayout.Tab, position: Int ->
            holder.binding.screenViewpager.setCurrentItem(tab.position, true)
        }
        tabLayoutMediator!!.attach()

        holder.itemView.setOnClickListener {
            onTopProductListener.onproductClick(position)
        }

        holder.binding.btnSchedule.setOnClickListener {
            onTopProductListener.onScheduleClick(position)
        }


        holder.binding.btnAdd.setOnClickListener {
            onCartListener?.onAddClickListener(position)
        }

        holder.binding.btnSub.setOnClickListener {
            onCartListener?.onSubClickListener(position)
        }
        holder.binding.btnBuy.setOnClickListener {
            product.variation!!.id?.let { it1 -> onCartListener?.onAddToCartListener(it1) }
        }

    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    class TopProductViewHolder(itemProductBinding: ItemProductsBinding) :
        RecyclerView.ViewHolder(itemProductBinding.root) {
        val binding = itemProductBinding

    }
}