package com.egorovfond.artillery.math

import com.egorovfond.artillery.data.Enemy
import com.egorovfond.artillery.data.Orudie
import com.egorovfond.artillery.data.Result
import com.egorovfond.artillery.database.DB
import com.egorovfond.artillery.presenter.BULLET_NOTHING
import com.egorovfond.artillery.presenter.Presenter
import kotlin.math.cos
import kotlin.math.sin

class Artilery {
    companion object {
        fun getMaxPricel(table: MutableList<Table>): Int{
            var max = 0
            for (elem in table ){
                if (elem.II > max) max = elem.II
            }

            return max
        }
        fun getMaxRange(table: MutableList<Table>, bullet: String, mortir: Boolean?): Int{
            var max = 0
            for (elem in table ){
                if (mortir != null && elem.mortir != mortir) {
                    continue
                }
                if (!(bullet.equals(BULLET_NOTHING) || bullet.equals(""))){
                    if (!elem.bulet.equals(bullet)){
                        continue
                    }
                }
                if (elem.D > max) max = elem.D
            }

            return max
        }

        fun getMinRange(table: MutableList<Table>, bullet: String, mortir: Boolean?): Int{
            var min = 0
            for (elem in table ){
                if (mortir != null && elem.mortir != mortir) {
                    continue
                }
                if (!(bullet.equals(BULLET_NOTHING) || bullet.equals(""))){
                    if (!elem.bulet.equals(bullet)){
                        continue
                    }
                }

                if (min == 0) min = elem.D
                if (elem.D < min) min = elem.D
            }

            return min
        }

        fun getTypeBullet_new(weapon: Orudie, distance: Int, mortir: Boolean, table: MutableList<Table>, base_: String, h: Int = 0): String {
            var bullet = BULLET_NOTHING

            var lastElem = Table()
            var nextElem = Table()

            bullet = weapon.bullet
            if (!bullet.isEmpty() && bullet != BULLET_NOTHING) return bullet

            for (elem in table ){
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                if (elem.D >= distance)
                    if(elem.D < nextElem.D || nextElem.D == 0) nextElem = elem

                if (elem.D <= distance)
                    if (elem.D > lastElem.D || lastElem.D == 0) lastElem = elem
            }

            if (lastElem.bulet == nextElem.bulet) return lastElem.bulet
            if (!lastElem.bulet.isEmpty() && lastElem.bulet != BULLET_NOTHING) return lastElem.bulet
            if (!nextElem.bulet.isEmpty() && nextElem.bulet != BULLET_NOTHING) return nextElem.bulet

            return bullet
        }

        fun getTypeBullet(weapon: Orudie, distance: Int, mortir: Boolean, table: MutableList<Table>, h: Int = 0): String {
            var bullet = BULLET_NOTHING
            var k = 0
            val base = 0
            val plus = 1
            val minus = -1

            var maxElem = Table()
            var minElem = table.first()

            for (elem in table) {
                if (!elem.nameTable.equals(weapon.base)) continue
                if (mortir) {
                    if (!elem.mortir) continue

                    if (k == plus) {
                        if (elem.D <= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == minus) {
                        if (elem.D >= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == base) {
                        if (elem.D > distance) k = plus
                        else {
                            if (elem.D == distance) {
                                bullet = elem.bulet
                                break
                            } else k = minus
                        }
                    }
                } else {
                    if (!elem.nameTable.equals(weapon.base)) continue
                    if (elem.mortir) continue

                    if (k == plus) {
                        if (elem.D <= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == minus) {
                        if (elem.D >= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == base) {
                        if (elem.D > distance) k = plus
                        else {
                            if (elem.D == distance) {
                                bullet = elem.bulet
                                break
                            } else k = minus
                        }
                    }
                }

            }
            return bullet
        }
        fun getKorCorner(rigth: Float, left: Float, range: Float): Int {
            val punktMetrov = range/1000
            val korRigth = rigth / punktMetrov
            val korLeft = left/ punktMetrov

            return korRigth.toInt() - korLeft.toInt()
        }
        fun getKorAim(weapon: Orudie, farther: Float, closer: Float, range: Float, bulletType: String, mortir: Boolean, table: MutableList<Table>): Int {
            val bullet = getBullet(bullet = bulletType, range = range.toInt(), table = table, mortir = mortir, base = weapon.base)
            val nextbullet = Table.nextBullet_new(bullet = bulletType, range = range.toInt(), table = table, mortir = mortir, base_ = weapon.base)
            val beforebullet = Table.beforeBullet_new(bullet = bulletType, range = range.toInt(), table = table, mortir = mortir, base_ = weapon.base)
            val korUp = farther * bullet.II_delt/50
            val korDown = closer * bullet.II_delt/50

            var znak = 1
            if ((nextbullet.D != 0 && bullet.II > nextbullet.II) || (beforebullet.D != 0 && bullet.II < beforebullet.II)){
                znak = -1
            }
            return korUp.toInt() * znak - korDown.toInt() * znak
        }
        /////////////////ДОП ФУНКЦИИ////////////////////////
        private fun getBullet(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base: String): Table {
            return Table.getBullet_new(bullet = bullet, range = range, table = table, mortir = mortir, base_ = base)
        }
        private fun getHeightCorrection(hOrudie: Int, hTarget: Int, mortir: Boolean, bullet: Table, range: Float, weapon: Orudie): Float {

            val IIH:Float = (hTarget-hOrudie)/100f*bullet.IIh_delt
            if (mortir) {
                //if (hTarget > hOrudie) return znak * IIH else return znak * -1f * IIH
                if(weapon.weapon.equals("M119")
                    || weapon.weapon.equals("D30")
                    || weapon.weapon.equals("M224")
                    || weapon.weapon.equals("2B9")
                    || weapon.weapon.equals("Type63")
                    || weapon.weapon.equals("2C3")
                    || weapon.weapon.equals("2C1")
                ) return -1f * IIH
                else return IIH
            }
            else {
//                val deltaH = hTarget-hOrudie
//                var A = deltaH/(range * 0.001f) * 0.95f
//                if (hTarget < hOrudie) A = -1f* A
//                //dПh = dH / 100 × dПh
//                var deltaHp = deltaH/100 * bullet.IIh_delt
//                if (hTarget < hOrudie) deltaHp = -1f* deltaHp
//
//                return A + deltaHp
                val deltaH = hTarget-hOrudie
                val IIh = (deltaH/100)*bullet.IIh_delt + (deltaH/(0.001f * range))
                return IIh
            }
        }
        ///поправка на продольный ветер
        private fun getWindCorrectionX(Aw: Float, bullet: Table, wind: Float): Float {
            val WX = wind * cos(Aw)
            return WX * bullet.Xw_delt
        }
        /// поправка на боковой ветер
        private fun getWindCorrectionZ(Aw: Float, bullet: Table, wind: Float): Float {
            val WY = wind * sin(Aw)
            return WY * bullet.Zw_delt
        }
        private fun getPhCorrection(ph: Float, bullet: Table): Float {
            return (bullet.baseH - ph)/10*bullet.Xh_delt
        }
        fun getAim(weapon: Orudie, target: Enemy, result: Result, table: MutableList<Table>):Int{
            val bullet: Table = getBullet(bullet = result.bullet, range = result.distace.toInt(), table = table, mortir = weapon.mortir, base = weapon.base)

            result.time = bullet.time

            if (bullet.D != result.distace.toInt()) return -1

            var XT = getTemperatureCorrection(Presenter.getPresenter().getPopravki().temp, bullet)
            var XH = getPhCorrection(Presenter.getPresenter().getPopravki().humidity, bullet)

            var XW:Float = 0f
            val Aw = WindData.getWinData(Presenter.getPresenter().getPopravki().wind_cross, result.azimut_target, weapon.mil)
            XW = getWindCorrectionX(Aw, bullet, Presenter.getPresenter().getPopravki().wind_speed)

            var II:Float = 0f
            II= getHeightCorrection(weapon.h, target.h, weapon.mortir, bullet, result.distace, weapon)

            val delta_X_summ = XT + XH + XW

            /// Чтобы компенсировать отклонение снаряда на X, следует изменить прицел на следующее значение П
            var P: Float = 0f
            if (weapon.mortir) {
                if(weapon.weapon.equals("M119")
                    || weapon.weapon.equals("M224")
                    || weapon.weapon.equals("D30")
                    || weapon.weapon.equals("M224")
                    || weapon.weapon.equals("2B9")
                    || weapon.weapon.equals("Type63")
                    || weapon.weapon.equals("2C3")
                    || weapon.weapon.equals("2C1")
                ) P = delta_X_summ/bullet.X_tis
                else P = -1f * delta_X_summ/bullet.X_tis
            }
            else P = -1f*delta_X_summ/bullet.X_tis

            return  Math.round((bullet.II + P + II))
        }
        ///////////// УГОЛ ///////////////
        private fun getTemperatureCorrection(t: Float, bullet: Table): Float {
            return (t-bullet.baseTemp)/10*bullet.Xt_delt
        }
        ////////////////////// ПРИЦЕЛ ///////////
        fun getCorner(weapon: Orudie, target: Enemy, result: Result, table: MutableList<Table>):Int{
            val bullet: Table = getBullet(result.bullet, result.distace.toInt(), table, weapon.mortir, base = weapon.base)

            var ZW: Float =0f
            val Aw = WindData.getWinData(Presenter.getPresenter().getPopravki().wind_cross, targetAzimut = result.azimut_target, weapon.mil)
            ZW = getWindCorrectionZ(Aw, bullet, Presenter.getPresenter().getPopravki().wind_speed)

            var Z_summ = ZW
            //if(weapon.mortir) Z_summ = Z_summ - bullet.Z else Z_summ = Z_summ + bullet.Z
            Z_summ = Z_summ + bullet.Z
            return  Math.round((result.ugol + Z_summ))
        }

        fun cor_getAim(weapon: Orudie, enemy: Enemy, result: Result){
            val distace = Azimut.getRangeByCoordinat(
                weapon.x,
                weapon.y,
                enemy.x_prilet,
                enemy.y_prilet
            )
            val cor_distace =  result.distace - distace

            val b = Math.atan2((enemy.y_prilet - weapon.y).toDouble(), (enemy.x_prilet - weapon.x).toDouble())
            val c = 180 * Math.PI/180 + b

            val x = weapon.x + cor_distace * cos(c)/1000
            val y = weapon.y + cor_distace * sin(c)/1000

            weapon.x = x.toFloat()
            weapon.y = y.toFloat()
        }
        fun cor_getCorner(weapon: Orudie, enemy: Enemy, result: Result){
            val azimut_target =
                Azimut.convertCoordinatToAzimut(
                    weapon.x,
                    weapon.y,
                    enemy.x_prilet,
                    enemy.y_prilet,
                    result.distace,
                    weapon.mil
                ).toInt()

            weapon.azimut_Dot = Azimut.getAzimutByTHHaveBussol(
                result.ugol,
                azimut_target,
                weapon.mil
            )
        }
    }
}