package com.teamx.mariaFoods.data.dataclasses.getAddress
import androidx.annotation.Keep

@Keep
data class GetAddressData(
    val Flag: Int?,
    val `data`: List<Data>
)