package com.teamx.mariaFoods.ui.fragments.payment

interface OnStripeCardListener {

    fun onDeleteClickListener(position: Int)
    fun onItemClickListener(position: Int?, id : String?)
}