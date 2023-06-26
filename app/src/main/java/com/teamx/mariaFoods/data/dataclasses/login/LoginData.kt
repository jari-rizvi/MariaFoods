package com.teamx.mariaFoods.data.dataclasses.login
import androidx.annotation.Keep
@Keep
data class LoginData(
    val AccessToken: String?,
    val Flag: Int?,
    val Message: String?,
    val User: User
)