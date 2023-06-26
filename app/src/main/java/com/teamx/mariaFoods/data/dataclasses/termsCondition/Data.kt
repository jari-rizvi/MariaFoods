package com.teamx.mariaFoods.data.dataclasses.termsCondition
import com.google.errorprone.annotations.Keep

@Keep
data class Data(
    val content: String?,
    val title: String?
)