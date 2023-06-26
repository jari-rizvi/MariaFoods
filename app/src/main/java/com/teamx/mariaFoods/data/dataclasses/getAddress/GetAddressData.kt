package com.teamx.mariaFoods.data.dataclasses.getAddress
import com.google.errorprone.annotations.Keep

@Keep
data class GetAddressData(
    val Flag: Int?,
    val `data`: List<Data>
)