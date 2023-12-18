package com.teamx.mariaFoods.data.dataclasses.getCart

import androidx.annotation.Keep
@Keep

data class Slot(
    val day: Int,
    val id: Int,
    val name: String,
    val timeline: String
)