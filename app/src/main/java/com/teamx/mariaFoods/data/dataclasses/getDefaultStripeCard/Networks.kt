package com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard
import androidx.annotation.Keep

@Keep
data class Networks(
    val available: List<String>,
    val preferred: Any
)