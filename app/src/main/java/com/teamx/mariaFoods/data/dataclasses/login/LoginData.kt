package com.teamx.mariaFoods.data.dataclasses.login
import com.google.errorprone.annotations.Keep

@Keep
data class LoginData(
    val AccessToken: String?,
    val Flag: Int?,
    val Message: String?,
    val User: User
)