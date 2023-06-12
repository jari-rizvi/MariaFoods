package com.teamx.mariaFoods.data.dataclasses.coupon

data class Data(
    val cartCount: Int,
    val carts: List<Cart>,
    val couponCode: String,
    val couponDiscount: String,
    val qty: Int,
    val subTotal: String,
    val vat: String,
    val Total: String,
    val delivery_charges: String,
)