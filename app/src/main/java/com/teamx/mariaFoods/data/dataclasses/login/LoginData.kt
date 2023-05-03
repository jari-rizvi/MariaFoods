package com.teamx.mariaFoods.data.dataclasses.login

data class LoginData(
    val AccessToken: String,
    val Flag: Int,
    val Message: String,
    val User: User
)