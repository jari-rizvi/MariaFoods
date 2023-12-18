package com.teamx.mariaFoods.data.dataclasses.wishList

import androidx.annotation.Keep
@Keep

data class Data(
    val id: Int,
    val item: Item,
    val product_id: Int
)