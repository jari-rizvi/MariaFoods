package com.teamx.mariaFoods.data.dataclasses.wishList

data class Variation(
    val code: String,
    val id: Int,
    val price: Int,
    val product_id: Int,
    val sku: String
)