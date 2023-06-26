package com.teamx.mariaFoods.data.models
import androidx.annotation.Keep

@Keep
data class ProductModel(
    val id: Int?,
    val title: String?,
    val price: String?,
    val category: String?,
    val description: String?,
    val image: String?,
    var isAddToCard: Boolean = false
)