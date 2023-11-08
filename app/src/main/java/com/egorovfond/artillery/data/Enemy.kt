package com.egorovfond.artillery.data

data class Enemy (
    @Volatile var nameTarget: String,
    @Volatile var x: Float = 0f,
    @Volatile var y: Float = 0f,
    @Volatile var h: Int = 0,
    @Volatile var k_use: Boolean = false,
    @Volatile var k_left: Int = 0,
    @Volatile var k_right: Int = 0,
    @Volatile var k_up: Int = 0,
    @Volatile var k_down: Int = 0,
    @Volatile var result: MutableList<Result> = mutableListOf<Result>(),
    @Volatile var position: Int = 0,

    @Volatile var x_prilet: Float = 0f,
    @Volatile var y_prilet: Float = 0f

)