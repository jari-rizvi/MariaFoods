package com.teamx.mariaFoods.data.dataclasses.addtocart

import androidx.annotation.Keep
@Keep
data class Data(
    val cartCount: Int?,
    val carts: List<Cart>,
    val couponDiscount: String?,
    val qty: Int?,
    val subTotal: String?
)