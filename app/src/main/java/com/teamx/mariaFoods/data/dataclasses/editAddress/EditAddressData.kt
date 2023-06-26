package com.teamx.mariaFoods.data.dataclasses.editAddress
import com.google.errorprone.annotations.Keep

@Keep
data class EditAddressData(
    val Flag: Int?,
    val `data`: Data
)