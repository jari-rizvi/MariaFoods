package com.teamx.mariaFoods.data.dataclasses.getStripecards
import androidx.annotation.Keep

@Keep
data class Networks(
    val available: List<String>,
    val preferred: Any
)