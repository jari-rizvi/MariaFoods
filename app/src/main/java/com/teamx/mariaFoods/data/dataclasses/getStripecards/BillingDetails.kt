package com.teamx.mariaFoods.data.dataclasses.getStripecards
import androidx.annotation.Keep

@Keep
data class BillingDetails(
    val address: Address,
    val email: Any?,
    val name: Any?,
    val phone: Any
)