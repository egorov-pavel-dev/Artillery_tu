package com.egorovfond.artillery.data

data class PravkiCorr (
    @Volatile var weapon: String,
    @Volatile var ugol_0: Float = 0f,
    @Volatile var ugol_750: Float = 0f,
    @Volatile var delta_0: Float = 0f,
    @Volatile var delta_750: Float = 0f,
    @Volatile var d: Int = 0
)