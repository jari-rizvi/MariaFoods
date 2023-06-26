package com.teamx.mariaFoods.data.dataclasses.getCart
import com.google.errorprone.annotations.Keep

@Keep
data class Data(
    val Total: String?,
    val cartCount: Int?,
    val carts: List<Cart>,
    val couponCode: String?,
    val couponDiscount: String?,
    val delivery_charges: String?,
    val qty: Int?,
    val subTotal: String?,
    val vat: String?
)