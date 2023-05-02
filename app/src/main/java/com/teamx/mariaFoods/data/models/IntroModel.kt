package com.teamx.mariaFoods.data.models

import androidx.room.Entity

@Entity
class IntroModel(
    val title: String,
    val description: String,
    val image: Int,
)