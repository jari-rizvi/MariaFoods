package com.teamx.mariaFoods.data.dataclasses.products

import androidx.annotation.Keep
@Keep
data class Data(
    val description: String?,
    val discount_end_date: Any?,
    val discount_start_date: Any?,
    val discount_type: String?,
    val discount_value: Int?,
    val feature_image: String?,
    val id: Int?,
    val max_price: Int?,
    val min_price: Int?,
    val name: String?,
    val product_images: List<String?>?,
    val short_description: String?,
    val slug: String?,
    val stock_qty: Int?,
    val variation: Variation?,
    var isChecked: Boolean?,
    var qty : Int =  1,
//    var isFav: Boolean = false,
    var is_wishlist: Boolean = false


)