package com.egorovfond.artillery.math

import java.lang.Math.toRadians

class Azimut {
    companion object{
        fun getAzimutByCompas(target: Int, enemy: Int, mil: Int):Int {
            /// переводим данные с компаса
            val A_target = mil/2 * target / 180
            val A_enemy = mil/2 * enemy / 180
            return getAzimutByTarget(A_target, A_enemy, mil)
        }
        fun getAzimutByTarget(target: Int, enemy: Int, mil: Int):Int {

            var Y: Int = enemy - target  + mil/2
            if (Y >= mil) Y = Y - mil
            if (Y < 0) Y = Y + mil

            return Y
        }

        fun getBussolUgol(ugolTarget: Int, ugolTH: Int, mil: Int):Int {

            var ugolFrtomTHToOrudie = mil/2 - ugolTH
            if (ugolFrtomTHToOrudie >= mil) ugolFrtomTHToOrudie = ugolFrtomTHToOrudie - mil
            if (ugolFrtomTHToOrudie < 0) ugolFrtomTHToOrudie = ugolFrtomTHToOrudie + mil

            var Y: Int = ugolFrtomTHToOrudie - (mil - ugolTarget)
            if (Y >= mil) Y = Y - mil
            if (Y < 0) Y = Y + mil

            return Y
        }

        fun convertCoordinatToAzimut(x1: Float, y1: Float, x2: Float, y2: Float, range: Float, mil: Int): Float{

            var result = convertCoordinatToCompas(x1, y1, x2, y2, range, mil)
            result = convertCompasToAzimut(result, mil)

            //if (mil == 6400) result = result * 6400 / 6000

            return result
        }
        fun convertCoordinatToCompas(x1: Float, y1: Float, x2: Float, y2: Float, range: Float, mil: Int): Float{

            val x_1 = x1 * 10
            val y_1 = y1 * 10
            val x_2 = x2 * 10
            val y_2 = y2 * 10

            var result = 0f

            result = ((180/Math.PI) * Math.atan2((x_2 - x_1).toDouble(), (y_2 -y_1).toDouble())).toFloat()
            if (result < 0) result = result + 360

            //if (mil == 6400) result = (result * 6400 / 6000)

            return result
        }

        fun convertCompasToAzimut(compas: Float, mil: Int):Float{
            return  mil/2f * compas / 180f
        }
        fun convertAzimutToCompas(azim: Float, mil: Int):Float{
            return azim * 180f /(mil/2f)
        }

        fun getRangeByCoordinat(xMinomet: Float, yMinomet: Float, xTarget: Float, yTarget: Float): Float {
            return Math.round(
                Math.sqrt(
                    Math.pow(
                        (xMinomet.toDouble()-xTarget.toDouble())
                        ,2.toDouble()
                    ) + Math.pow(
                        (yMinomet.toDouble()-yTarget.toDouble())
                        ,2.toDouble()
                    )
                )*1000.toDouble()
            ).toFloat()
        }
        fun getXCoordinat(xMinomet: Float, range: Float, ugol: Int, mil: Int): Float{
            val ugolcompas = convertAzimutToCompas(ugol.toFloat(), mil)

            return ((xMinomet * 1000 + range * kotlin.math.sin(toRadians(ugolcompas.toDouble())))/1000).toFloat()
        }
        fun getYCoordinat(yMinomet: Float, range: Float, ugol: Int, mil: Int): Float{
            val ugolcompas = convertAzimutToCompas(ugol.toFloat(), mil)

            return ((yMinomet * 1000 + range * kotlin.math.cos(toRadians(ugolcompas.toDouble())))/1000).toFloat()
        }
        fun getAzimutByTHHaveBussol(bussol: Int, enemy: Int, mil: Int): Int{
            var Y: Int = enemy - bussol  + mil/2
            if (Y >= mil) Y = Y - mil
            if (Y < 0) Y = Y + mil

            return Y
        }

        fun getMirrorUgol(ugol: Int, mil: Int): Int{
            var Y: Int = ugol + mil/2
            if (Y >= mil) Y = Y - mil
            if (Y < 0) Y = Y + mil

            return Y
        }
    }
}