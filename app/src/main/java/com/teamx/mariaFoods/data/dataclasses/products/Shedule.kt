package com.teamx.mariaFoods.data.dataclasses.products

data class Shedule(
    val order_days: String,
    val time_slots: List<TimeSlot>
)