package com.teamx.mariaFoods.data.dataclasses.checkout
import com.google.errorprone.annotations.Keep

@Keep
data class CheckoutData(
    val Flag: Int?,
    val client_secreat: String?
)