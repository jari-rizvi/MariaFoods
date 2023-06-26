package com.teamx.mariaFoods.data.dataclasses.addtocart
import com.google.errorprone.annotations.Keep

@Keep
data class AddToCartData(
    val Flag: Int?,
    val Message: String?,
    val `data`: Data
)