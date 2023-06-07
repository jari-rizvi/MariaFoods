package com.teamx.mariaFoods.data.dataclasses.products

data class OrderDay(
    val date: String,
    val day: Int = 0,
    val name: String,
    var isChecked : Boolean
)