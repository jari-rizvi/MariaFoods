package com.teamx.mariaFoods.data.models
import com.google.errorprone.annotations.Keep

@Keep
data class MusicResponseModel(
    val resultCount: Int?,
    val results: List<MusicModel>?,
)