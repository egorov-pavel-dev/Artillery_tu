package com.egorovfond.artillery.database.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeaponEntity(
    @PrimaryKey(autoGenerate = false)
    val name: String,
    val mil: Int
)