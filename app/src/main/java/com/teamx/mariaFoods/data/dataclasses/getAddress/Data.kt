package com.teamx.mariaFoods.data.dataclasses.getAddress

data class Data(
    val address_1: String,
    val address_2: String,
    val city: String,
    val country: String,
    val id: Int,
    val is_default: Int,
    val postal: String,
    val state: String,
    val user_id: Int
)