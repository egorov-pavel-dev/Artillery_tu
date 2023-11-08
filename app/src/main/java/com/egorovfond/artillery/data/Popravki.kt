package com.egorovfond.artillery.data

data class Popravki (
    @Volatile var wind_speed: Float = 0f,
    @Volatile var wind_cross: Int = 0,
    @Volatile var humidity: Float = 1013.25f,
    @Volatile var temp: Float = 15f,
    @Volatile var radius: Int = 50
)