package com.teamx.mariaFoods.data.dataclasses.addWishlist

import androidx.annotation.Keep
@Keep

data class Data(
    val id: Int,
    val product: Product,
    val product_id: Int
)