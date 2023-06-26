package com.teamx.mariaFoods.data.dataclasses.products

import com.google.errorprone.annotations.Keep

@Keep
data class ProductsData(
    val Flag: Int?,
    val data: List<Data?>?,
    val shedule: Shedule?
)