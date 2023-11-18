package com.egorovfond.artillery.data

data class Result (
    val orudie: Orudie,
    @Volatile var pricel: Int = 0,
    @Volatile var ugol: Int = 0,
    @Volatile var distace: Float = 0f,
    @Volatile var azimut_target: Int = 0,
    @Volatile var bullet: String = "",
    @Volatile var resultUgol: Int = 0,
    @Volatile var resultPricel: Int = 0,
    @Volatile var deltaUgol: Int = 0,
    @Volatile var deltaPricel: Int = 0,
    @Volatile var time: Float = 0f
)