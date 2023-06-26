package com.teamx.mariaFoods.data.dataclasses.getStripecards
import com.google.errorprone.annotations.Keep

@Keep
data class StripeCardsData(
    val Flag: Int?,
    val `data`: List<Data>
)