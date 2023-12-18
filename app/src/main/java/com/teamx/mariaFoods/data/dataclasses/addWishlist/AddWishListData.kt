package com.teamx.mariaFoods.data.dataclasses.addWishlist

import androidx.annotation.Keep
@Keep

data class AddWishListData(
    val Flag: Int,
    val Message: String,
    val `data`: Data
)