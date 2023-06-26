package com.teamx.mariaFoods.data.dataclasses.uploadProfile
import com.google.errorprone.annotations.Keep

@Keep
data class UploadProfileData(
    val Flag: Int?,
    val Messgae: String?,
    val avatar: String?
)