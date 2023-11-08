package com.egorovfond.artillery.data

data class Orudie(
    @Volatile var nameOrudie: String,
    @Volatile var weapon: String = "",
    @Volatile var x: Float = 0f,
    @Volatile var y: Float = 0f,
    @Volatile var x_Dot: Float = 0f,
    @Volatile var y_Dot: Float = 0f,
    @Volatile var azimut_Dot: Int = 0,
    @Volatile var h: Int = 0,
    @Volatile var mortir: Boolean = true,
    @Volatile var mil: Int = 6000,
    @Volatile var position: Int = 0,
    @Volatile var bullet: String = "",
    @Volatile var base: String = "",
    @Volatile var weapon_save_x: Float = 0f,
    @Volatile var weapon_save_y: Float = 0f,
    @Volatile var weapon_save_azimut: Int = 0

)