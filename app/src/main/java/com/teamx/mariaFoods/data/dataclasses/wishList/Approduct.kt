package com.teamx.mariaFoods.data.dataclasses.wishList

data class Approduct(
    val description: String,
    val discount_end_date: Int,
    val discount_start_date: Int,
    val discount_type: Any,
    val discount_value: Int,
    val feature_image: String,
    val id: Int,
    val max_price: Int,
    val min_price: Int,
    val name: String,
    val priority: Int,
    val product_images: List<String>,
    val short_description: String,
    val slug: String,
    val stock_qty: Int,
    val variation: Variation
)