package com.teamx.mariaFoods.data.dataclasses.orderHistory
import com.google.errorprone.annotations.Keep

@Keep
data class OrderData(
    val Flag: Int?,
    val `data`: Data
)