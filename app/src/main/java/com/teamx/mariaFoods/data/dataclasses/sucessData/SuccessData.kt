package com.teamx.mariaFoods.data.dataclasses.sucessData
import com.google.errorprone.annotations.Keep

@Keep
data class SuccessData(
    val Flag: Int?,
    val Message: String?
)