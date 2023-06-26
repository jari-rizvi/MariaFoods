package com.teamx.mariaFoods.data.dataclasses.notification
import com.google.errorprone.annotations.Keep

@Keep
data class May(
    val body: String?,
    val time: String?,
    val title: String?
)