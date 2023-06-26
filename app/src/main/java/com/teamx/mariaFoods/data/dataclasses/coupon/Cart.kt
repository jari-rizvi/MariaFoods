package com.teamx.mariaFoods.data.dataclasses.coupon
import com.google.errorprone.annotations.Keep

@Keep
data class Cart(
    val id: Int?,
    val product: Product?,
    val qty: Int
)