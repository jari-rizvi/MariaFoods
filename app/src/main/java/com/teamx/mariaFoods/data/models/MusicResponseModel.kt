package com.teamx.mariaFoods.data.models
import androidx.annotation.Keep
@Keep
data class MusicResponseModel(
    val resultCount: Int?,
    val results: List<MusicModel>?,
)