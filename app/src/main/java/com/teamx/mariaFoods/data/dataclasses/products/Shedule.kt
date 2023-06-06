package com.teamx.mariaFoods.data.dataclasses.products

data class Shedule(
    val order_days: List<OrderDay>,
    val time_slots: List<TimeSlot>
)