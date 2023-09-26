package com.teamx.mariaFoods.data.dataclasses.removeCart

data class Data(
    val Total: String,
    val cartCount: Int,
    val carts: List<Cart>,
    val couponCode: String,
    val couponDiscount: String,
    val delivery_charges: String,
    val qty: Int,
    val slot: Slot,
    val subTotal: String,
    val vat: String
)