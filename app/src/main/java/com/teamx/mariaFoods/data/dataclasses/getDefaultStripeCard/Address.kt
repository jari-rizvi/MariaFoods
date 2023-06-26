package com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard
import androidx.annotation.Keep

@Keep
data class Address(
    val city: Any?,
    val country: String?,
    val line1: Any?,
    val line2: Any?,
    val postal_code: Any?,
    val state: Any
)