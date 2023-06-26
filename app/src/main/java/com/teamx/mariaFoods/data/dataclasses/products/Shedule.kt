package com.teamx.mariaFoods.data.dataclasses.products

import com.google.errorprone.annotations.Keep

@Keep
data class Shedule(
    val order_days: List<OrderDay?>?,
    val time_slots: List<TimeSlot?>?
)