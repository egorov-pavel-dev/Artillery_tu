package com.egorovfond.artillery.math

import com.egorovfond.artillery.data.PravkiCorr
import com.egorovfond.artillery.data.Weapons
import com.egorovfond.artillery.data.localTable.LocalData2B14

class PricelCorr {
    companion object {
        fun getCorrPricel(mil: Int, weapon: String, target:Int, D: Int): Int{
           if (weapon == Weapons.weapon_2B14){
               val tableCorr = LocalData2B14.table
               var znak = LocalData2B14.position

               return getCorner(mil = mil, target = target, tablecor = tableCorr, znak = znak, D = D)
            }

            return 0
        }

        fun getCorner(mil: Int, target:Int, tablecor: PravkiCorr, znak: Int, D: Int): Int{
            var ugol_0 = tablecor.ugol_0 - tablecor.delta_0 * D
            var ugol_750 = tablecor.ugol_750 - tablecor.delta_750 * D
            if(ugol_0 < 0) ugol_0 = 0f
            if(ugol_750 < 0) ugol_750 = 0f
            val delta = (ugol_750 - ugol_0) / 750

            if (target > 0 && target <= mil / 4){
                val ugol = delta * target  + ugol_0
                return (ugol * znak).toInt()
            }
            if (target > mil / 4 && target <= mil / 2){
                val ugol = (delta * mil / 4) - (delta * (target - mil / 4)  + ugol_0)
                return (ugol * znak).toInt()
            }
            if (target > mil / 2 && target <= (mil / 2 + mil / 4)){
                val ugol = (delta * (target - mil / 2)  + ugol_0)
                return (ugol * znak * -1).toInt()
            }
            if ((target > (mil / 2 + mil / 4) && target <= mil)|| target == 0){
                val ugol = (delta * mil / 4) - (delta * (target - (mil / 2 + mil / 4))  + ugol_0)
                return (ugol * znak * -1).toInt()
            }
            return 0
        }
    }
}