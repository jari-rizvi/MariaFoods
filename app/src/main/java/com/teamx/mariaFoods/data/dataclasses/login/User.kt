package com.teamx.mariaFoods.data.dataclasses.login

data class User(
    var avatar: String,
    val email: String,
    val email_or_otp_verified: Int,
    val first_name: String,
    val id: Int,
    val last_name: String,
    val name: String,
    val phone: String,
    val provider_id: String
)