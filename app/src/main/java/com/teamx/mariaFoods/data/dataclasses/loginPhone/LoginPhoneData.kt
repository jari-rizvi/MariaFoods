package com.teamx.mariaFoods.data.dataclasses.loginPhone
import androidx.annotation.Keep

@Keep
data class LoginPhoneData(
    val Flag: Int?,
    val Message: String?,
    val phone: String?
)