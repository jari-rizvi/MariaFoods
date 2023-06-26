package com.teamx.mariaFoods.data.dataclasses.resetPass
import com.google.errorprone.annotations.Keep

@Keep
data class ResetPassData(
    val Flag: Int?,
    val Message: String?,
    val email: String?
)