package com.teamx.mariaFoods.data.dataclasses.loginPhone
import com.google.errorprone.annotations.Keep

@Keep
data class LoginPhoneData(
    val Flag: Int?,
    val Message: String?,
    val phone: String?
)