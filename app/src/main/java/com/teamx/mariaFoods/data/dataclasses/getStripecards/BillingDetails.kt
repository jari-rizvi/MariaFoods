package com.teamx.mariaFoods.data.dataclasses.getStripecards
import com.google.errorprone.annotations.Keep

@Keep
data class BillingDetails(
    val address: Address,
    val email: Any?,
    val name: Any?,
    val phone: Any
)