package com.teamx.mariaFoods.data.dataclasses.orderHistory

data class ScheduledDeliveryInfo(
    val id: Int,
    val last_order_time: String,
    val scheduled_date: String,
    val timeline: String
)