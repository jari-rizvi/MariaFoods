package com.teamx.mariaFoods.data.dataclasses.signup

data class SignupData(
    val AccessToken: String,
    val Flag: Int,
    val Message: String,
    val User: com.teamx.mariaFoods.data.dataclasses.login.User
)