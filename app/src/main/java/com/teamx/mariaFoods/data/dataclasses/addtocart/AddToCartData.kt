package com.teamx.mariaFoods.data.dataclasses.addtocart

import androidx.annotation.Keep
@Keep

data class AddToCartData(
    val Flag: Int,
    val Message: String,
    val `data`: Data
)