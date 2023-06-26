package com.teamx.mariaFoods.data.dataclasses.getAddress
import com.google.errorprone.annotations.Keep

@Keep
data class Data(
    val address_1: String? = "",
    val address_2: String? = "",
    val city: String?,
    val country: String?,
    val id: Int?,
    val is_default: Int?,
    val lat: Any?,
    val long: Any?,
    val name: String?,
    val postal: String?,
    val state: String?,
    val user_id: Int
)