package com.teamx.mariaFoods.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "music_table")
class MusicModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0


)