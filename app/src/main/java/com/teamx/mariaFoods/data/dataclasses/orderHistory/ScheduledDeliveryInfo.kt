package com.teamx.mariaFoods.data.dataclasses.orderHistory

data class ScheduledDeliveryInfo(
    val id: Int,
    val scheduled_date: String,
    val timeline: String
)