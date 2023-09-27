package com.teamx.mariaFoods.ui.fragments.Dashboard.home

interface OnCartListener {

    fun onAddClickListener(position: Int)
    fun onSubClickListener(position: Int)
    fun onAddToCartListener(id: Int)
    fun onRemoveToCartListener(position: Int)


    fun onQuantityChange(position: Int, quantity: Int)
}