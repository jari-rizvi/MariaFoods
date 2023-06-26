package com.teamx.mariaFoods.data.dataclasses.editAddress
import androidx.annotation.Keep

@Keep
data class Data(
    val address_1: String?,
    val address_2: String?,
    val city: String?,
    val country: Any?,
    val id: Int?,
    val is_default: Int?,
    val lat: String?,
    val long: String?,
    val name: String?,
    val postal: String?,
    val state: Any?,
    val user_id: Int
)