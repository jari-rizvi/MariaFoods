package com.teamx.mariaFoods.data.dataclasses.products

data class Variation(
    val code: String,
    val id: Int,
    val price: Double,
    val product_id: Int,
    val sku: String
)