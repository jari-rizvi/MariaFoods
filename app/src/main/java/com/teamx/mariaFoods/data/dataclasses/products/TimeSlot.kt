package com.teamx.mariaFoods.data.dataclasses.products

data class TimeSlot(
    val id: Int = 0,
    val name: String,
    val timeline: String,
    var isChecked: Boolean,
    var last_order_time: String
)