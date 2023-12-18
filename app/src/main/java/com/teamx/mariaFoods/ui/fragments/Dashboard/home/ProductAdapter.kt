package com.teamx.mariaFoods.ui.fragments.Dashboard.home

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import com.teamx.mariaFoods.MainApplication.Companion.context
import com.teamx.mariaFoods.R
import com.teamx.mariaFoods.constants.NetworkCallPoints
import com.teamx.mariaFoods.data.dataclasses.products.Data
import com.teamx.mariaFoods.data.local.datastore.DataStoreProvider
import com.teamx.mariaFoods.databinding.ItemProductsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class ProductAdapter(
    val arrayList: ArrayList<Data>,
    private val onTopProductListener: OnProductListener,
    val onCartListener: OnCartListener
) : RecyclerView.Adapter<ProductAdapter.TopProductViewHolder>() {

    lateinit var ProductBannerAdapter: ProductBannersAdapter
    private var tabLayoutMediator: TabLayoutMediator? = null
    lateinit var dataStoreProvider: DataStoreProvider


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopProductViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val itemTopProductBinding = ItemProductsBinding.inflate(inflater, parent, false)
        return TopProductViewHolder(itemTopProductBinding)

    }

    override fun onBindViewHolder(holder: TopProductViewHolder, position: Int) {

        dataStoreProvider = DataStoreProvider(context)


        val productBannner: Data = arrayList[position]

        val product: Data = arrayList[position]

        holder.binding.productName.text = product.name

        holder.binding.price.text = "${product.max_price} AED"

//       val quanti = product.qty ?: 1
        if (product.qty < 1) {
            product.qty = 1
        }
        holder.binding.textView19.text = "${product.qty}"



        holder.binding.productDescription.text = product.short_description

        Picasso.get().load(product.feature_image).into(holder.binding.img)

        if (product.is_wishlist) {
            Log.d("true", "onBindViewHolder: ${product.is_wishlist}")
            holder.binding.btnFav.setImageResource(R.drawable.wishlist_selected)
        } else {
            holder.binding.btnFav.setImageResource(R.drawable.wishlist_circle)
            Log.d("true", "onBindViewHolder: ${product.is_wishlist}")

        }


        var token: String?? = null
        CoroutineScope(Dispatchers.Main).launch {

            dataStoreProvider.token.collect {
                Log.d("Databsae Token", "CoroutineScope ${it}")

                Log.d("dataStoreProvider", "subscribeToNetworkLiveData: $it")

                token = it

                NetworkCallPoints.TOKENER = token.toString()

                try {
                    if (token.isNullOrBlank()) {
                        holder.binding.btnFav.visibility = View.GONE

                    } else {

                    }

                } catch (e: Exception) {

                }

            }


        }


        holder.itemView.setOnClickListener {
            onTopProductListener.onproductClick(position)
        }

        holder.binding.btnSchedule.setOnClickListener {
            onTopProductListener.onScheduleClick(position)
        }

        holder.binding.btnFav.setOnClickListener {
            onTopProductListener.onAddFavClick(position, product.is_wishlist)
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