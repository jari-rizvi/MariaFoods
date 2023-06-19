package com.teamx.mariaFoods.ui.fragments.payment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.teamx.mariaFoods.data.dataclasses.getStripecards.Data
import com.teamx.mariaFoods.databinding.ItemPaymentCardsBinding

class PaymentAdapter(
    private val cardsArrayList: ArrayList<Data>, val onStripeCardListener: OnStripeCardListener
) : RecyclerView.Adapter<AddressViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressViewHolder {
        return AddressViewHolder(
            ItemPaymentCardsBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )

    }

    override fun onBindViewHolder(holder: AddressViewHolder, position: Int) {


        val cardsList: Data? = cardsArrayList[position]

        holder.bind.paymentName.text = try {
            "*** *** *** ${cardsList?.card?.last4}"
        } catch (e: Exception) {
            ""
        }

        if (cardsList?.card?.brand == "visa") {
            holder.bind.paymentVisa.visibility = View.VISIBLE
        } else if (cardsList?.card?.brand == "master card") {
            holder.bind.paymentaster.visibility = View.VISIBLE

        }


//        holder.bind.postal.text = try {
//            "Postal code ${cardsList?.postal}"
//        } catch (e: Exception) {
//            ""
//        }


        holder.bind.btnDlt.setOnClickListener {
            onStripeCardListener.onDeleteClickListener(position)
        }

        holder.itemView.setOnClickListener {
            if (cardsList != null) {
                onStripeCardListener.onItemClickListener(position,cardsList.id)
            }
        }


    }

    override fun getItemCount(): Int {
        return cardsArrayList.size
    }
}

class AddressViewHolder(private var binding: ItemPaymentCardsBinding) :
    RecyclerView.ViewHolder(binding.root) {

    val bind = binding

}