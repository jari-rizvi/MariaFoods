package com.teamx.mariaFoods.data.dataclasses.wishList

import androidx.annotation.Keep
@Keep

data class GetWishlist(
    val Flag: Int,
    val `data`: List<Data>
)