package com.teamx.mariaFoods.data.dataclasses.products

data class TimeSlot(
    val id: Int,
    val name: String,
    val timeline: String,
    var isChecked: Boolean
)