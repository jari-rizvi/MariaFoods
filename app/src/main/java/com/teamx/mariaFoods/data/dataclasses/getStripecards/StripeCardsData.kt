package com.teamx.mariaFoods.data.dataclasses.getStripecards
import androidx.annotation.Keep
@Keep
data class StripeCardsData(
    val Flag: Int?,
    val `data`: List<Data>
)