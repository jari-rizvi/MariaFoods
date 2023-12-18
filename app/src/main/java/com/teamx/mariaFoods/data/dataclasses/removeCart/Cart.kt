package com.teamx.mariaFoods.data.dataclasses.removeCart

import androidx.annotation.Keep
@Keep

data class Cart(
    val id: Int,
    val product: Product,
    val qty: Int
)