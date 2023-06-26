package com.teamx.mariaFoods.data.dataclasses.products

import androidx.annotation.Keep
@Keep
data class TimeSlot(
    val id: Int?,
    val last_order_time: String?,
    val name: String?, val timeline: String?, var isChecked: Boolean = false,
    )