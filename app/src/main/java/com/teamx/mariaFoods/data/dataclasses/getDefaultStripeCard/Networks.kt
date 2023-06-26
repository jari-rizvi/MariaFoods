package com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard
import com.google.errorprone.annotations.Keep

@Keep
data class Networks(
    val available: List<String>,
    val preferred: Any
)