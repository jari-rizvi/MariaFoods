package com.teamx.mariaFoods.data.dataclasses.resetPass
import androidx.annotation.Keep
@Keep
data class ResetPassData(
    val Flag: Int?,
    val Message: String?,
    val email: String?
)