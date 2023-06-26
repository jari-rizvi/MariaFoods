package com.teamx.mariaFoods.data.dataclasses.banners
import com.google.errorprone.annotations.Keep

@Keep
data class BannerListData(
    val Flag: Int?,
    val data: List<Data?>?
)