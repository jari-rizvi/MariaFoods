package com.teamx.mariaFoods.data.dataclasses.getCart

data class Cart(
    val id: Int,
    val product: Product,
    var qty: Int
)