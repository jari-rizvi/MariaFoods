package com.teamx.mariaFoods.data.dataclasses.getDefaultStripeCard
import com.google.errorprone.annotations.Keep

@Keep
data class GetDefaultStripeCard(
    val Flag: Int?,
    val `data`: Data
)