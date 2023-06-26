package com.teamx.mariaFoods.data.dataclasses.products

import com.google.errorprone.annotations.Keep

@Keep
data class OrderDay(
    val date: String?,
    val day: Int?,
    val name: String?,
    var isChecked: Boolean = false

    )