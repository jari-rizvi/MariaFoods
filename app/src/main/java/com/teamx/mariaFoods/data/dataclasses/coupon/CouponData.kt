package com.teamx.mariaFoods.data.dataclasses.coupon
import com.google.errorprone.annotations.Keep

@Keep
data class CouponData(
    val Flag: Int?,
    val Message: String?,
    val `data`: Data
)