package com.teamx.mariaFoods.data.dataclasses.products

data class Data(
    val description: String,
    val discount_end_date: Any,
    val discount_start_date: Any,
    val discount_type: String,
    val discount_value: Int,
    val feature_image: String,
    val id: Int,
    val max_price: Double,
    val min_price: Double,
    val name: String,
    val product_images: List<String>,
    val short_description: String,
    val slug: String,
    val stock_qty: Int,
    val variation: Variation,
    var qty : Int =  1
)