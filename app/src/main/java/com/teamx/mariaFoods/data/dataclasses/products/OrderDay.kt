package com.teamx.mariaFoods.data.dataclasses.products

data class OrderDay(
    val date: String,
    val day: Int,
    val name: String,
    var isChecked : Boolean
)