package com.teamx.mariaFoods.data.dataclasses.notification
import com.google.errorprone.annotations.Keep

@Keep
data class Data(
    var  Jun: List<Jun>?,
    var  May: List<May>?
)