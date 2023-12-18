package com.teamx.mariaFoods.data.dataclasses.removeCart

import androidx.annotation.Keep
@Keep

data class RemoveCartData(
    val Flag: Int,
    val Message: String,
    val `data`: Data
)