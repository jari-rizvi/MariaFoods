package com.teamx.mariaFoods.data.dataclasses.getCart
import com.google.errorprone.annotations.Keep

@Keep
data class GetCartData(
    val Flag: Int?,
    val `data`: Data
)