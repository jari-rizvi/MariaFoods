package com.teamx.mariaFoods.data.dataclasses.signup

import com.teamx.mariaFoods.data.dataclasses.login.User

data class SignupData(
    val AccessToken: String,
    val Flag: Int,
    val Message: String,
    val User: User
)