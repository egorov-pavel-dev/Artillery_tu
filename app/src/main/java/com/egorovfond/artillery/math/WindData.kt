package com.egorovfond.artillery.math

data class WindData(
    val wind: Float,
    val windAzimut: Int,
    val targetAzimut: Int,
    val Aw: Int
){
    companion object{
        fun getWinData(windAzimut: Int, targetAzimut: Int, mil: Int): Float {
            var Aw = 0f
            var aTarget = targetAzimut
            var aWind = windAzimut

            aWind = mil/2 * windAzimut / 180

            if (aTarget - aWind < 0) Aw = (aTarget + mil -aWind).toFloat()
            else Aw = (aTarget - aWind).toFloat()

            return Aw
        }
    }
}