package com.egorovfond.artillery.math

import com.egorovfond.artillery.data.localTable.*
import com.egorovfond.artillery.database.room.entity.TableEntity
import com.egorovfond.artillery.presenter.BULLET_NOTHING

data class Table(
    var mortir: Boolean = false,
    var bulet: String = BULLET_NOTHING,
    var D: Int = 0, /// Дальность
    var II: Int = 0, // Прицел
    var X_tis: Float = 1f, // Изменение дальности на 1 тыс Хтыс
    var II_delt: Float = 1f, // Изменение прицела на 50м дальности
    var IIh_delt: Float = 1f, // Изменение прицела на 100м высоты
    var Z: Float = 0f, // Изменение угла. Деривация
    var Zw_delt: Float = 1f, // Изменение угла боковой ветер на 1м/с
    var Xw_delt: Float = 1f, // Изменение прицела, продольный ветер 1 м/с
    var Xt_delt: Float = 1f, // Изменение температуры воздуха на 10 градусов. норма 15.
    var Xh_delt: Float = 1f, // Изенение прицела, давление воздуха на 10hPa, норма 1013.25
    val id: Int = 0,
    val nameTable: String = "0м/15С/1013.25гПа",
    val baseTemp: Float = 15f,
    val baseH: Float = 1013.25f,
    val baseHeigth: Int = 0,
    val time: Float = 0f
    ) {
    companion object {
        fun getBullet_new(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var lastElem = Table()
            var nextElem = Table()
            var currentElem = Table()

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                if (elem.D >= range)
                    if(elem.D < nextElem.D || nextElem.D == 0) nextElem = elem

                if (elem.D <= range)
                    if (elem.D > lastElem.D || lastElem.D == 0) lastElem = elem
            }

            if (lastElem.D != nextElem.D) {
                        val D = range
                        var II: Float = 0f
                        var X_tis: Float = 0f
                        var II_delt: Float = 0f
                        var IIh_delt: Float = 0f
                        var Z: Float = 0f
                        var Zw_delt: Float = 0f
                        var Xw_delt: Float = 0f
                        var Xt_delt: Float = 0f
                        var Xh_delt: Float = 0f
                        var id = 0
                        var nameTable = ""
                        var baseTemp = 0f
                        var baseH = 0f
                        var baseHeigth = 0
                        var time = 0f

                        if (lastElem.bulet == BULLET_NOTHING || nextElem.bulet == BULLET_NOTHING){

                        }else if (nextElem.D > lastElem.D) {
                            II = ((nextElem.II - lastElem.II) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.II).toFloat()
                            X_tis = ((nextElem.X_tis - lastElem.X_tis) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.X_tis)
                            II_delt = ((nextElem.II_delt - lastElem.II_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.II_delt)
                            IIh_delt = ((nextElem.IIh_delt - lastElem.IIh_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.IIh_delt)
                            Z = ((nextElem.Z - lastElem.Z) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.Z)
                            Zw_delt = ((nextElem.Zw_delt - lastElem.Zw_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.Zw_delt)
                            Xw_delt = ((nextElem.Xw_delt - lastElem.Xw_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.Xw_delt)
                            Xt_delt = ((nextElem.Xt_delt - lastElem.Xt_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.Xt_delt)
                            Xh_delt = ((nextElem.Xh_delt - lastElem.Xh_delt) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.Xh_delt)
                            id = nextElem.id
                            nameTable = nextElem.nameTable
                            baseTemp = nextElem.baseTemp
                            baseH = nextElem.baseH
                            baseHeigth = nextElem.baseHeigth
                            time = ((nextElem.time - lastElem.time) * (D - lastElem.D) / (nextElem.D - lastElem.D) + lastElem.time)

                        }
                        else if (nextElem.D == 0){
                            val before = beforeBullet_new(bullet = bullet, range = lastElem.D, table = table, mortir = mortir, base_ = base_)

                            /// Изменяем прицел на значение изменения прицела на каждые 50 метров дальности
                            /// остальные параметры берем от последнего значения
                            var znak_II = 1
                            if (before.D != 0 && lastElem.II < before.II) znak_II = -1
                            II = (znak_II * lastElem.II_delt * (D - lastElem.D) / 50 + lastElem.II).toFloat()

//                            var znak_X_tis = 1
//                            if (before.D != 0 && lastElem.X_tis < before.X_tis) znak_X_tis = -1
//                            X_tis = (znak_X_tis * (lastElem.X_tis) * (D - lastElem.D) / D + lastElem.X_tis)
                            X_tis = lastElem.X_tis

//                            var znak_II_delt = 1
//                            if (before.D != 0 && lastElem.II_delt < before.II_delt) znak_II_delt = -1
//                            II_delt = (znak_II_delt * (lastElem.II_delt) * (D - lastElem.D) / D + lastElem.II_delt)
                            II_delt = lastElem.II_delt

//                            var znak_IIh_delt = 1
//                            if (before.D != 0 && lastElem.IIh_delt < before.IIh_delt) znak_IIh_delt = -1
//                            IIh_delt = (znak_IIh_delt * (lastElem.IIh_delt) * (D - lastElem.D) / D + lastElem.IIh_delt)
                            IIh_delt = lastElem.IIh_delt

//                            var znak_Z = 1
//                            if (before.D != 0 && lastElem.Z < before.Z) znak_Z = -1
//                            Z = (znak_Z * (lastElem.Z) * (D - lastElem.D) / D + lastElem.Z)
                            Z = lastElem.Z

//                            var znak_Zw_delt = 1
//                            if (before.D != 0 && lastElem.Zw_delt < before.Zw_delt) znak_Zw_delt = -1
//                            Zw_delt = (znak_Zw_delt * (lastElem.Zw_delt) * (D - lastElem.D) / D + lastElem.Zw_delt)
                            Zw_delt = lastElem.Zw_delt

//                            var znak_Xw_delt = 1
//                            if (before.D != 0 && lastElem.Xw_delt < before.Xw_delt) znak_Xw_delt = -1
//                            Xw_delt = (znak_Xw_delt * (lastElem.Xw_delt) * (D - lastElem.D) / D + lastElem.Xw_delt)
                            Xw_delt = lastElem.Xw_delt

//                            var znak_Xt_delt = 1
//                            if (before.D != 0 && lastElem.Xt_delt < before.Xt_delt) znak_Xt_delt = -1
//                            Xt_delt = (znak_Xt_delt * (lastElem.Xt_delt) * (D - lastElem.D) / D + lastElem.Xt_delt)
                            Xt_delt = lastElem.Xt_delt

//                            var znak_Xh_delt = 1
//                            if (before.D != 0 && lastElem.Xh_delt < before.Xh_delt) znak_Xh_delt = -1
//                            Xh_delt = (znak_Xh_delt * (lastElem.Xh_delt) * (D - lastElem.D) / D + lastElem.Xh_delt)
                            Xh_delt = lastElem.Xh_delt

                            id = lastElem.id
                            nameTable = lastElem.nameTable
                            baseTemp = lastElem.baseTemp
                            baseH = lastElem.baseH
                            baseHeigth = lastElem.baseHeigth
                            time = lastElem.time

                        }
                        else if (lastElem.D == 0){
                            val next = nextBullet_new(bullet = bullet, range = nextElem.D, table = table, mortir = mortir, base_ = base_)

                            var znak_II = 1
                            if (next.D != 0 && nextElem.II > next.II) znak_II = -1
                            II = (znak_II * (nextElem.II_delt) * (nextElem.D - D) / 50 + nextElem.II).toFloat()


//                            var znak_X_tis = 1
//                            if (next.D != 0 && nextElem.X_tis > next.X_tis) znak_X_tis = -1
//                            X_tis = (znak_X_tis * (nextElem.X_tis) * (nextElem.D - D) / nextElem.D + nextElem.X_tis)
                            X_tis = nextElem.X_tis

//                            var znak_II_delt = 1
//                            if (next.D != 0 && nextElem.II_delt > next.II_delt) znak_II_delt = -1
//                            II_delt = (znak_II_delt * (nextElem.II_delt) * (nextElem.D - D) / nextElem.D + nextElem.II_delt)
                            II_delt = nextElem.II_delt

//                            var znak_IIh_delt = 1
//                            if (next.D != 0 && nextElem.IIh_delt > next.IIh_delt) znak_IIh_delt = -1
//                            IIh_delt = (znak_IIh_delt * (nextElem.IIh_delt) * (nextElem.D - D) / nextElem.D + nextElem.IIh_delt)
                            IIh_delt = nextElem.IIh_delt

//                            var znak_Z = 1
//                            if (next.D != 0 && nextElem.Z > next.Z) znak_Z = -1
//                            Z = (znak_Z * (nextElem.Z) * (nextElem.D - D) / nextElem.D + nextElem.Z)
                            Z = nextElem.Z

//                            var znak_Zw_delt = 1
//                            if (next.D != 0 && nextElem.Zw_delt > next.Zw_delt) znak_Zw_delt = -1
//                            Zw_delt = (znak_Zw_delt * (nextElem.Zw_delt) * (nextElem.D - D) / nextElem.D + nextElem.Zw_delt)
                            Zw_delt = nextElem.Zw_delt

//                            var znak_Xw_delt = 1
//                            if (next.D != 0 && nextElem.Xw_delt > next.Xw_delt) znak_Xw_delt = -1
//                            Xw_delt = (znak_Xw_delt * (nextElem.Xw_delt) * (nextElem.D - D) / nextElem.D + nextElem.Xw_delt)
                            Xw_delt = nextElem.Xw_delt

//                            var znak_Xt_delt = 1
//                            if (next.D != 0 && nextElem.Xt_delt > next.Xt_delt) znak_Xt_delt = -1
//                            Xt_delt = (znak_Xt_delt * (nextElem.Xt_delt) * (nextElem.D - D) / nextElem.D + nextElem.Xt_delt)
                            Xt_delt = nextElem.Xt_delt

//                            var znak_Xh_delt = 1
//                            if (next.D != 0 && nextElem.Xh_delt > next.Xh_delt) znak_Xh_delt = -1
//                            Xh_delt = (znak_Xh_delt * (nextElem.Xh_delt) * (nextElem.D - D) / nextElem.D + nextElem.Xh_delt)
                            Xh_delt = nextElem.Xh_delt

                            id = nextElem.id
                            nameTable = nextElem.nameTable
                            baseTemp = nextElem.baseTemp
                            baseH = nextElem.baseH
                            baseHeigth = nextElem.baseHeigth
                            time = nextElem.time
                        }


                currentElem = Table(
                            mortir = mortir,
                            bulet = bullet,
                            D = D,
                            II = II.toInt(),
                            X_tis = X_tis,
                            II_delt = II_delt,
                            IIh_delt = IIh_delt,
                            Z = Z,
                            Zw_delt = Zw_delt,
                            Xw_delt = Xw_delt,
                            Xt_delt = Xt_delt,
                            Xh_delt = Xh_delt,
                            id = id,
                            nameTable = nameTable,
                            baseTemp = baseTemp,
                            baseH = baseH,
                            baseHeigth = baseHeigth,
                            time = time
                        )
                    } else currentElem = lastElem
            return currentElem
        }
        fun nextBullet_new(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var nextElem = Table()

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                if (elem.D >= range)
                    if(elem.D < nextElem.D || nextElem.D == 0)
                    {
                        nextElem = elem
                        return nextElem
                    }
            }

            return nextElem
        }
        fun beforeBullet_new(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var lastElem = Table()

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                if (elem.D <= range)
                    if (elem.D > lastElem.D || lastElem.D == 0) {
                        lastElem = elem
                        return lastElem
                    }
            }

            return lastElem
        }

        fun getBullet(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var lastElem: Table? = null
            var nextElem: Table? = null
            var currentElem: Table? = null

            var k = 0
            val base = 0
            val plus = 1
            val minus = -1

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                nextElem = elem
                if (k == plus) {
                    if (elem.D < range) {
                        break
                    }else {
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == minus){
                    if (elem.D > range) {
                        break
                    }else{
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == base) {
                    if (elem.D > range){
                        lastElem = elem
                        k = plus
                    }
                    else {
                        if (elem.D == range){
                            nextElem = elem
                            lastElem = elem
                            break
                        }else {
                            lastElem = elem
                            k = minus
                        }
                    }
                }
            }

            lastElem?.let { last ->
                nextElem?.let {next ->
                    if (last!!.D != next!!.D) {
                        val D = range
                        var II: Float = 0f
                        var X_tis: Float = 0f
                        var II_delt: Float = 0f
                        var IIh_delt: Float = 0f
                        var Z: Float = 0f
                        var Zw_delt: Float = 0f
                        var Xw_delt: Float = 0f
                        var Xt_delt: Float = 0f
                        var Xh_delt: Float = 0f

                        if (next.D > last.D) {
                            II = ((next.II - last.II) * (D - last.D) / (next.D - last.D) + last.II).toFloat()
                            X_tis = ((next.X_tis - last.X_tis) * (D - last.D) / (next.D - last.D) + last.X_tis).toFloat()
                            II_delt = ((next.II_delt - last.II_delt) * (D - last.D) / (next.D - last.D) + last.II_delt).toFloat()
                            IIh_delt = ((next.IIh_delt - last.IIh_delt) * (D - last.D) / (next.D - last.D) + last.IIh_delt).toFloat()
                            Z = ((next.Z - last.Z) * (D - last.D) / (next.D - last.D) + last.Z).toFloat()
                            Zw_delt = ((next.Zw_delt - last.Zw_delt) * (D - last.D) / (next.D - last.D) + last.Zw_delt).toFloat()
                            Xw_delt = ((next.Xw_delt - last.Xw_delt) * (D - last.D) / (next.D - last.D) + last.Xw_delt).toFloat()
                            Xt_delt = ((next.Xt_delt - last.Xt_delt) * (D - last.D) / (next.D - last.D) + last.Xt_delt).toFloat()
                            Xh_delt = ((next.Xh_delt - last.Xh_delt) * (D - last.D) / (next.D - last.D) + last.Xh_delt).toFloat()
                        }
                        else {
                            II = ((last.II - next.II) * (D - next.D) / (last.D - next.D) + next.II).toFloat()
                            X_tis = ((last.X_tis - next.X_tis) * (D - next.D) / (last.D - next.D) + next.X_tis).toFloat()
                            II_delt = ((last.II_delt - next.II_delt) * (D - next.D) / (last.D - next.D) + next.II_delt).toFloat()
                            IIh_delt = ((last.IIh_delt - next.IIh_delt) * (D - next.D) / (last.D - next.D) + next.IIh_delt).toFloat()
                            Z = ((last.Z - next.Z) * (D - next.D) / (last.D - next.D) + next.Z).toFloat()
                            Zw_delt = ((last.Zw_delt - next.Zw_delt) * (D - next.D) / (last.D - next.D) + next.Zw_delt).toFloat()
                            Xw_delt = ((last.Xw_delt - next.Xw_delt) * (D - next.D) / (last.D - next.D) + next.Xw_delt).toFloat()
                            Xt_delt = ((last.Xt_delt - next.Xt_delt) * (D - next.D) / (last.D - next.D) + next.Xt_delt).toFloat()
                            Xh_delt = ((last.Xh_delt - next.Xh_delt) * (D - next.D) / (last.D - next.D) + next.Xh_delt).toFloat()
                        }

                        currentElem = Table(
                            mortir = mortir,
                            bulet = bullet,
                            D = D,
                            II = II.toInt(),
                            X_tis = X_tis,
                            II_delt = II_delt,
                            IIh_delt = IIh_delt,
                            Z = Z,
                            Zw_delt = Zw_delt,
                            Xw_delt = Xw_delt,
                            Xt_delt = Xt_delt,
                            Xh_delt = Xh_delt,
                            id = last.id,
                            nameTable = last.nameTable,
                            baseTemp = last.baseTemp,
                            baseH = last.baseH,
                            baseHeigth = last.baseHeigth
                        )
                    } else currentElem = last
                }
            }
            return currentElem ?: Table()
        }
        fun nextBullet(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var lastElem: Table? = null
            var nextElem: Table? = null
            var currentElem: Table? = null

            var k = 0
            val base = 0
            val plus = 1
            val minus = -1

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                nextElem = elem
                if (k == plus) {
                    if (elem.D < range) {
                        break
                    }else {
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == minus){
                    if (elem.D > range) {
                        break
                    }else{
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == base) {
                    if (elem.D > range){
                        lastElem = elem
                        k = plus
                    }
                    else {
                        if (elem.D == range){
                            nextElem = elem
                            lastElem = elem
                            break
                        }else {
                            lastElem = elem
                            k = minus
                        }
                    }
                }
            }

            lastElem?.let { last ->
                nextElem?.let {next ->
                    if (last!!.D != next!!.D) {
                        val D = range
                        var II: Float = 0f
                        var X_tis: Float = 0f
                        var II_delt: Float = 0f
                        var IIh_delt: Float = 0f
                        var Z: Float = 0f
                        var Zw_delt: Float = 0f
                        var Xw_delt: Float = 0f
                        var Xt_delt: Float = 0f
                        var Xh_delt: Float = 0f

                        if (next.D > last.D) {
                            II = ((next.II - last.II) * (D - last.D) / (next.D - last.D) + last.II).toFloat()
                            X_tis = ((next.X_tis - last.X_tis) * (D - last.D) / (next.D - last.D) + last.X_tis).toFloat()
                            II_delt = ((next.II_delt - last.II_delt) * (D - last.D) / (next.D - last.D) + last.II_delt).toFloat()
                            IIh_delt = ((next.IIh_delt - last.IIh_delt) * (D - last.D) / (next.D - last.D) + last.IIh_delt).toFloat()
                            Z = ((next.Z - last.Z) * (D - last.D) / (next.D - last.D) + last.Z).toFloat()
                            Zw_delt = ((next.Zw_delt - last.Zw_delt) * (D - last.D) / (next.D - last.D) + last.Zw_delt).toFloat()
                            Xw_delt = ((next.Xw_delt - last.Xw_delt) * (D - last.D) / (next.D - last.D) + last.Xw_delt).toFloat()
                            Xt_delt = ((next.Xt_delt - last.Xt_delt) * (D - last.D) / (next.D - last.D) + last.Xt_delt).toFloat()
                            Xh_delt = ((next.Xh_delt - last.Xh_delt) * (D - last.D) / (next.D - last.D) + last.Xh_delt).toFloat()
                        }
                        else {
                            II = ((last.II - next.II) * (D - next.D) / (last.D - next.D) + next.II).toFloat()
                            X_tis = ((last.X_tis - next.X_tis) * (D - next.D) / (last.D - next.D) + next.X_tis).toFloat()
                            II_delt = ((last.II_delt - next.II_delt) * (D - next.D) / (last.D - next.D) + next.II_delt).toFloat()
                            IIh_delt = ((last.IIh_delt - next.IIh_delt) * (D - next.D) / (last.D - next.D) + next.IIh_delt).toFloat()
                            Z = ((last.Z - next.Z) * (D - next.D) / (last.D - next.D) + next.Z).toFloat()
                            Zw_delt = ((last.Zw_delt - next.Zw_delt) * (D - next.D) / (last.D - next.D) + next.Zw_delt).toFloat()
                            Xw_delt = ((last.Xw_delt - next.Xw_delt) * (D - next.D) / (last.D - next.D) + next.Xw_delt).toFloat()
                            Xt_delt = ((last.Xt_delt - next.Xt_delt) * (D - next.D) / (last.D - next.D) + next.Xt_delt).toFloat()
                            Xh_delt = ((last.Xh_delt - next.Xh_delt) * (D - next.D) / (last.D - next.D) + next.Xh_delt).toFloat()
                        }

                        currentElem = Table(
                            mortir = mortir,
                            bulet = bullet,
                            D = D,
                            II = II.toInt(),
                            X_tis = X_tis,
                            II_delt = II_delt,
                            IIh_delt = IIh_delt,
                            Z = Z,
                            Zw_delt = Zw_delt,
                            Xw_delt = Xw_delt,
                            Xt_delt = Xt_delt,
                            Xh_delt = Xh_delt,
                            id = last.id,
                            nameTable = last.nameTable,
                            baseTemp = last.baseTemp,
                            baseH = last.baseH,
                            baseHeigth = last.baseHeigth
                        )
                    } else currentElem = last
                }
            }
            return nextElem ?: Table()
        }
        fun beforeBullet(bullet: String, range: Int, table: MutableList<Table>, mortir: Boolean, base_: String): Table{
            var lastElem: Table? = null
            var nextElem: Table? = null
            var currentElem: Table? = null

            var k = 0
            val base = 0
            val plus = 1
            val minus = -1

            for (elem in table ){
                if (!elem.bulet.equals(bullet)) continue
                if (!elem.nameTable.equals(base_)) continue
                if (elem.mortir != mortir) continue

                nextElem = elem
                if (k == plus) {
                    if (elem.D < range) {
                        break
                    }else {
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == minus){
                    if (elem.D > range) {
                        break
                    }else{
                        if (elem.D == range) {
                            lastElem = elem
                            break
                        }
                        else lastElem = elem
                    }
                }
                if (k == base) {
                    if (elem.D > range){
                        lastElem = elem
                        k = plus
                    }
                    else {
                        if (elem.D == range){
                            nextElem = elem
                            lastElem = elem
                            break
                        }else {
                            lastElem = elem
                            k = minus
                        }
                    }
                }
            }

            lastElem?.let { last ->
                nextElem?.let {next ->
                    if (last!!.D != next!!.D) {
                        val D = range
                        var II: Float = 0f
                        var X_tis: Float = 0f
                        var II_delt: Float = 0f
                        var IIh_delt: Float = 0f
                        var Z: Float = 0f
                        var Zw_delt: Float = 0f
                        var Xw_delt: Float = 0f
                        var Xt_delt: Float = 0f
                        var Xh_delt: Float = 0f

                        if (next.D > last.D) {
                            II = ((next.II - last.II) * (D - last.D) / (next.D - last.D) + last.II).toFloat()
                            X_tis = ((next.X_tis - last.X_tis) * (D - last.D) / (next.D - last.D) + last.X_tis).toFloat()
                            II_delt = ((next.II_delt - last.II_delt) * (D - last.D) / (next.D - last.D) + last.II_delt).toFloat()
                            IIh_delt = ((next.IIh_delt - last.IIh_delt) * (D - last.D) / (next.D - last.D) + last.IIh_delt).toFloat()
                            Z = ((next.Z - last.Z) * (D - last.D) / (next.D - last.D) + last.Z).toFloat()
                            Zw_delt = ((next.Zw_delt - last.Zw_delt) * (D - last.D) / (next.D - last.D) + last.Zw_delt).toFloat()
                            Xw_delt = ((next.Xw_delt - last.Xw_delt) * (D - last.D) / (next.D - last.D) + last.Xw_delt).toFloat()
                            Xt_delt = ((next.Xt_delt - last.Xt_delt) * (D - last.D) / (next.D - last.D) + last.Xt_delt).toFloat()
                            Xh_delt = ((next.Xh_delt - last.Xh_delt) * (D - last.D) / (next.D - last.D) + last.Xh_delt).toFloat()
                        }
                        else {
                            II = ((last.II - next.II) * (D - next.D) / (last.D - next.D) + next.II).toFloat()
                            X_tis = ((last.X_tis - next.X_tis) * (D - next.D) / (last.D - next.D) + next.X_tis).toFloat()
                            II_delt = ((last.II_delt - next.II_delt) * (D - next.D) / (last.D - next.D) + next.II_delt).toFloat()
                            IIh_delt = ((last.IIh_delt - next.IIh_delt) * (D - next.D) / (last.D - next.D) + next.IIh_delt).toFloat()
                            Z = ((last.Z - next.Z) * (D - next.D) / (last.D - next.D) + next.Z).toFloat()
                            Zw_delt = ((last.Zw_delt - next.Zw_delt) * (D - next.D) / (last.D - next.D) + next.Zw_delt).toFloat()
                            Xw_delt = ((last.Xw_delt - next.Xw_delt) * (D - next.D) / (last.D - next.D) + next.Xw_delt).toFloat()
                            Xt_delt = ((last.Xt_delt - next.Xt_delt) * (D - next.D) / (last.D - next.D) + next.Xt_delt).toFloat()
                            Xh_delt = ((last.Xh_delt - next.Xh_delt) * (D - next.D) / (last.D - next.D) + next.Xh_delt).toFloat()
                        }

                        currentElem = Table(
                            mortir,
                            bullet,
                            D,
                            II.toInt(),
                            X_tis,
                            II_delt,
                            IIh_delt,
                            Z,
                            Zw_delt,
                            Xw_delt,
                            Xt_delt,
                            Xh_delt,
                            id = last.id,
                            nameTable = last.nameTable,
                            baseTemp = last.baseTemp,
                            baseH = last.baseH,
                            baseHeigth = last.baseHeigth
                        )
                    } else currentElem = last
                }
            }
            return lastElem ?: Table()
        }


        fun getBulletName(table: MutableList<Table>): List<String> {
            val mutableList = linkedSetOf<String>()
            for (i in table){
                mutableList.add(i.bulet)
            }

            return mutableList.toList()
        }
        fun checkBullet(bullet_: String, table_: MutableList<Table>, distance: Int, mortir: Boolean): Boolean {
            var bullet = BULLET_NOTHING
            var k = 0
            val base = 0
            val plus = 1
            val minus = -1

            for (elem in table_ ){
                if (!elem.bulet.equals(bullet_)) continue
                if (mortir) {
                    if (!elem.mortir) continue

                    if (k == plus) {
                        if (elem.D <= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == minus){
                        if (elem.D >= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == base) {
                        if (elem.D > distance) k = plus
                        else {
                            if (elem.D == distance){
                                bullet = elem.bulet
                                break
                            }else k = minus
                        }
                    }
                }
                else {
                    if (elem.mortir) continue

                    if (k == plus) {
                        if (elem.D <= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == minus){
                        if (elem.D >= distance) {
                            bullet = elem.bulet
                            break
                        }
                    }
                    if (k == base) {
                        if (elem.D > distance) k = plus
                        else {
                            if (elem.D == distance){
                                bullet = elem.bulet
                                break
                            }else k = minus
                        }
                    }
                }
            }
            return bullet.equals(bullet_)
        }

        fun toEntity(table: Table, weapon: String): TableEntity = TableEntity(
            id = table.id,
            mortir = table.mortir,
            bulet = table.bulet,
            D = table.D,
            II = table.II,
            X_tis = table.X_tis,
            II_delt = table.II_delt,
            IIh_delt = table.IIh_delt,
            Z = table.Z,
            Zw_delt = table.Zw_delt,
            Xw_delt = table.Xw_delt,
            Xt_delt = table.Xt_delt,
            Xh_delt = table.Xh_delt,
            nameTable = table.nameTable,
            baseH = table.baseH,
            baseTemp = table.baseTemp,
            baseHeigth = table.baseHeigth,
            time = table.time,
            weapon = weapon
        )

        fun toTable(table: TableEntity): Table = Table(
            id = table.id,
            mortir = table.mortir,
            bulet = table.bulet,
            D = table.D,
            II = table.II,
            X_tis = table.X_tis,
            II_delt = table.II_delt,
            IIh_delt = table.IIh_delt,
            Z = table.Z,
            Zw_delt = table.Zw_delt,
            Xw_delt = table.Xw_delt,
            Xt_delt = table.Xt_delt,
            Xh_delt = table.Xh_delt,
            nameTable = table.nameTable,
            baseH = table.baseH,
            baseTemp = table.baseTemp,
            baseHeigth = table.baseHeigth,
            time = table.time
            )

        val table2B9 = LocalTable2B9.table
        val table2B14 = LocalTable2B14.table
        val tableD30 = LocalTableD30.table
        val tableM119 = LocalTableM119.table
        val tableM224 = LocalTableM224.table
        val tableM252 = LocalTableM252.table
        val table120mm = LocalTable120mm.table
        val table2C3 = LocalTable2C3.table
        val table2C1 = LocalTable2C1.table
        val tableType63 = LocalTableType63.table
        val tableSPG9 = LocalTableSPG9.table
        val tableBM21 = LocalTableBM21.table
    }
}