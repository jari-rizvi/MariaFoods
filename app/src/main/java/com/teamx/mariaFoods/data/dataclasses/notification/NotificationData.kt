package com.teamx.mariaFoods.data.dataclasses.notification
import com.google.errorprone.annotations.Keep

@Keep
data class NotificationData(
    val Flag: Int?,
    val data: String?
)