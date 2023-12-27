package com.egorovfond.artillery.database

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.egorovfond.artillery.*
import com.egorovfond.artillery.data.Enemy
import com.egorovfond.artillery.data.Result
import com.egorovfond.artillery.data.Weapons
import com.egorovfond.artillery.database.room.dao.TableDao
import com.egorovfond.artillery.database.room.dao.WeaponDao
import com.egorovfond.artillery.database.room.entity.WeaponEntity
import com.egorovfond.artillery.math.Artilery
import com.egorovfond.artillery.math.Azimut
import com.egorovfond.artillery.math.PricelCorr
import com.egorovfond.artillery.math.Table
import com.egorovfond.artillery.presenter.BULLET_NOTHING
import com.egorovfond.artillery.presenter.Presenter
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.schedulers.Schedulers


class DB {
    companion object {
        private val TAG = "DB"
        private val liveDatagetWeaponList: MutableLiveData<MutableList<WeaponEntity>> = MutableLiveData()
        private val liveDatagetWeaponTable: MutableLiveData<MutableList<Table>> = MutableLiveData()
        private val liveDataUpdateResult: MutableLiveData<Boolean> = MutableLiveData()
        private val liveDatagetBullet: MutableLiveData<MutableList<String>> = MutableLiveData()
        private val liveDatagetBase: MutableLiveData<MutableList<String>> = MutableLiveData()
        private val liveDataInicialize: MutableLiveData<Boolean> = MutableLiveData()

        @Volatile private var lock: Boolean = false

        var weaponDao: WeaponDao? = null
        var tableDao: TableDao? = null

        private val baseWeaponList = mutableListOf(Weapons.weapon_120mm, Weapons.weapon_2B14, Weapons.weapon_2B9, Weapons.weapon_D30, Weapons.weapon_M119, Weapons.weapon_M224, Weapons.weapon_M252, Weapons.weapon_Type63, Weapons.weapon_2C3, Weapons.weapon_2C1, Weapons.weapon_SPG9, Weapons.weapon_BM21)
        val weapon = mutableListOf(Weapons.weapon_120mm, Weapons.weapon_2B14, Weapons.weapon_2B9, Weapons.weapon_D30, Weapons.weapon_M119, Weapons.weapon_M224, Weapons.weapon_M252, Weapons.weapon_Type63, Weapons.weapon_2C3, Weapons.weapon_2C1, Weapons.weapon_SPG9, Weapons.weapon_BM21)
        private val table = mutableListOf<Table>()

        fun subscribegetWeaponList(): MutableLiveData<MutableList<WeaponEntity>> = liveDatagetWeaponList
        fun subscribegetWeaponTable(): MutableLiveData<MutableList<Table>> = liveDatagetWeaponTable
        fun subscribeUpdateResult(): MutableLiveData<Boolean> = liveDataUpdateResult
        fun subscribegetBullet(): MutableLiveData<MutableList<String>> = liveDatagetBullet
        fun subscribegetBase(): MutableLiveData<MutableList<String>> = liveDatagetBase
        fun subscribeInicialize(): MutableLiveData<Boolean> = liveDataInicialize

        fun getWeaponTable(weapon: String, tableWeapon: MutableList<Table>) {
            synchronized(DB::class) {
                /// Работа с базой
                tableDao?.let {
                    /// Проверим наличие данных в базе
                    it.getTableByWeapon(weapon = weapon)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                table.clear()
                                if (it.size != 0)
                                {
                                    val dbTable = mutableListOf<Table>()
                                    for (elem in it) {
                                        dbTable.add(Table.toTable(elem))
                                    }

                                    table.clear()
                                    table.addAll(dbTable)

                                    liveDatagetWeaponTable.postValue(table)
                                }else{
                                    initBullet(weapon, tableWeapon)
                                }
                            },
                            {
                                Log.d(TAG, "getWeaponTable: ${it}")
                            }
                        )
                }
            }
        }
        fun updateTableWeapoon(weapon: String, bullet: String, base: String) {
            synchronized(DB::class) {
                /// Работа с базой
                tableDao?.let {
                    /// Проверим наличие данных в базе
                    it.getTableByWeaponAndBullet(weapon = weapon, bullet = bullet, base = base)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                table.clear()


                                if (it.size != 0) {
                                    val dbTable = mutableListOf<Table>()
                                    for (elem in it) {
                                        dbTable.add(Table.toTable(elem))
                                    }

                                    table.clear()
                                    table.addAll(dbTable)
                                }

                                liveDatagetWeaponTable.postValue(table)
                            },
                            {
                                Log.d(TAG, "getWeaponTable: ${it}")
                            }
                        )
                }
            }
        }
        fun getWeaponList(){
            /// Работа с базой
            synchronized(DB::class) {
                weaponDao?.let {
                    it.getAllWeapon()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(
                            {
                                if (it.size != 0) {
                                    weapon.clear()
                                    for (elem in it) weapon.add(elem.name)
                                }

                                liveDatagetWeaponList.postValue(it.toMutableList())
                            },
                            {}
                        )
                }
            }
        }
        fun getWeaponBullet(weapon: String, base: String) = tableDao?.let {
            it.getWeaponBullet(weapon = weapon, base = base)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        liveDatagetBullet.postValue(it.toMutableList())
                    },
                    {

                    }
                )
        }
        fun getWeaponBase(weapon: String) = tableDao?.let {
            it.getWeaponBase(weapon = weapon)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(
                    {
                        liveDatagetBase.postValue(it.toMutableList())
                    },
                    {

                    }
                )
        }
        fun addWeapon(nameWeapon: String, mil: Int = 6000){
            /// Работа с базой
            weaponDao?.let {
                it.insertWeapon(WeaponEntity(name = nameWeapon, mil = mil))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            Log.d(TAG, "addWeapon: success")
                        },
                        {
                            Log.d(TAG, "addWeapon: $it")
                        }
                    )
            }
        }
        fun saveWeaponTable(table: MutableList<Table>, weapon: String){

            /// Работа с базой
            for (elem in table){
                tableDao?.let {
                    if (elem.id == 0) it.insertTable(Table.toEntity(table = elem, weapon = weapon))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                Log.d(TAG, "saveWeaponTable: success")
                            },
                            {
                                Log.d(TAG, "saveWeaponTable: $it")
                            }
                        )
                    else it.updateTable(Table.toEntity(table = elem, weapon = weapon))
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
                                Log.d(TAG, "saveWeaponTable: success")
                            },
                            {
                                Log.d(TAG, "saveWeaponTable: $it")
                            }
                        )
                }
            }
        }

        fun updateTargetList(target: Enemy, result: Result) {
            tableDao?.let {
                /// Проверим наличие данных в базе
                it.getTableByWeapon(weapon = result.orudie.weapon)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            val dbTable = mutableListOf<Table>()

                            if (it.size != 0)
                            {
                                for (elem in it) {
                                    dbTable.add(Table.toTable(elem))
                                }
                            }


                            val maxPricel = Artilery.getMaxPricel(table = dbTable)

                            result.bullet = Artilery.getTypeBullet_new(
                               weapon = result.orudie,
                               distance = result.distace.toInt(),
                               mortir = result.orudie.mortir,
                               table = dbTable,
                               base_ = result.orudie.base
                            )

                            var korCorner = 0
                            var korAim = 0

                            if (target.k_use) {
                                korCorner = Artilery.getKorCorner(
                                    target.k_right.toFloat(),
                                    target.k_left.toFloat(),
                                    result.distace
                                )
                                korAim = Artilery.getKorAim(
                                    result.orudie,
                                    target.k_up.toFloat(),
                                    target.k_down.toFloat(),
                                    result.distace,
                                    result.bullet,
                                    result.orudie.mortir,
                                    dbTable
                                )
                            }
                            result.deltaUgol = Artilery.getKorCorner(
                                rigth = Presenter.getPresenter().getPopravki().radius.toFloat(),
                                left= 0f,
                                range = result.distace
                            )
                            if (result.deltaUgol < 0 || result.deltaUgol >= result.orudie.mil) result.deltaUgol = 0

                            result.deltaPricel = Artilery.getKorAim(
                                result.orudie,
                                Presenter.getPresenter().getPopravki().radius.toFloat(),
                                0f,
                                result.distace,
                                result.bullet,
                                result.orudie.mortir,
                                dbTable
                            )
                            if (result.deltaPricel < 0 || result.deltaPricel >= maxPricel) result.deltaPricel = 0

                            result.resultPricel = Artilery.getAim(
                                weapon = result.orudie,
                                target = target,
                                result = result,
                                table = dbTable
                            ) + korAim

                            result.resultUgol = Artilery.getCorner(
                                weapon = result.orudie,
                                target = target,
                                result = result,
                                table = dbTable
                            ) + korCorner

//                              result.resultUgol = result.resultUgol + PricelCorr.getCorrPricel(
//                                mil = result.orudie.mil,
//                                weapon = result.orudie.weapon,
//                                target = result.azimut_target,
//                                D = Azimut.getRangeByCoordinat(
//                                    result.orudie.x,
//                                    result.orudie.y,
//                                    result.orudie.x_Dot,
//                                    result.orudie.y_Dot
//                                ).toInt()
//                            )

                            if (result.resultUgol < 0) result.resultUgol = 0

                            if (result.resultPricel < 0 || result.resultPricel > maxPricel) {
                                result.resultPricel = 0
                                result.deltaPricel = 0
                                result.resultUgol = 0
                                result.deltaUgol = 0
                                result.time = 0f

                                result.bullet = BULLET_NOTHING
                            }
                            if (result.resultPricel < 0) result.resultPricel = 0

                            liveDataUpdateResult.postValue(true)
                        },
                        {
                            Log.d(TAG, "getWeaponTable: ${it}")
                        }
                    )
            }
        }

        fun initializeDB(){
            weaponDao?.let{
                it.getAllWeapon()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        {
                            if (it.size != 0) dropTables()
                            else tableDao?.let {
                                it.getAllWeapon()
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(
                                        {
                                            liveDataInicialize.postValue(true)
                                        },
                                        {

                                        }
                                    )
                            }
                        },
                        {

                        }
                    )
            }
        }
        fun firstLoad(){
            /// перенесено в модуль создания базы
            insertData()
        }
        fun insertData() {
                weaponDao?.let {
                    for (weaponName in baseWeaponList) {
                        initWeapon(weaponName = weaponName)
                    }
                }
        }
        fun initWeapon(weaponName: String){
               weaponDao?.let {
                   var mil = 6000
                   if (weaponName.equals(Weapons.weapon_M119)
                       || weaponName.equals(Weapons.weapon_M224)
                       || weaponName.equals(Weapons.weapon_M252)
                   ) mil = 6400

                   val weaponEntity =
                        WeaponEntity(name = weaponName, mil = mil)

                    it.insertWeapon(weapon = weaponEntity)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(
                            {
//                                tableDao?.let {
//                                    if (weaponName.equals("120mm")) {
//                                        for (table in Table.table120mm)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("2B14")) {
//                                        for (table in Table.table2B14)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("2B9")) {
//                                        for (table in Table.table2B9)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("D30")) {
//                                        for (table in Table.tableD30)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//
//                                                     },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("M119")) {
//                                        for (table in Table.tableM119)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                     },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("M224")) {
//                                        for (table in Table.tableM224)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("M252")) {
//                                        for (table in Table.tableM252)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("Type63")) {
//                                        for (table in Table.tableType63)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("2C3")) {
//                                        for (table in Table.table2C3)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                    if (weaponName.equals("2C1")) {
//                                        for (table in Table.table2C1)
//                                            it.insertTable(
//                                                Table.toEntity(
//                                                    table = table,
//                                                    weapon = weaponName
//                                                )
//                                            )
//                                                .subscribeOn(Schedulers.io())
//                                                .observeOn(AndroidSchedulers.mainThread())
//                                                .subscribe(
//                                                    {
//                                                    },
//                                                    {
//
//                                                    }
//                                                )
//                                    }
//                                }
                            },
                            {
                            }
                        )
            }
        }
        fun dropTables(){
            tableDao?.let {
                it.delete()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        weaponDao?.let {
                            it.delete()
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe({
                                    liveDataInicialize.postValue(true)
                                },{})
                        }
                    },{

                    })
            }
        }

        fun initBullet(weaponName: String, tableWeapon: MutableList<Table>){
            tableDao?.let {
                if(tableWeapon.size == 0) {
                    if (weaponName.equals(Weapons.weapon_120mm)) {
                        for (table_ in Table.table120mm)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.table120mm)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_2B14)) {
                        for (table_ in Table.table2B14)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.table2B14)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_2B9)) {
                        for (table_ in Table.table2B9)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.table2B9)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_D30)) {
                        for (table_ in Table.tableD30)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableD30)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_M119)) {
                        for (table_ in Table.tableM119)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableM119)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_M224)) {
                        for (table_ in Table.tableM224)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableM224)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_M252)) {
                        for (table_ in Table.tableM252)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableM252)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_Type63)) {
                        for (table_ in Table.tableType63)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableType63)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_2C3)) {
                        for (table_ in Table.table2C3)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.table2C3)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_2C1)) {
                        for (table_ in Table.table2C1)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.table2C1)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_SPG9)) {
                        for (table_ in Table.tableSPG9)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableSPG9)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                    if (weaponName.equals(Weapons.weapon_BM21)) {
                        for (table_ in Table.tableBM21)
                            it.insertTable(
                                Table.toEntity(
                                    table = table_,
                                    weapon = weaponName
                                )
                            )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                    {
                                        table.clear()
                                        table.addAll(Table.tableBM21)
                                        liveDatagetWeaponTable.postValue(table)
                                    },
                                    {

                                    }
                                )
                    }
                }else{
                    for (table_ in tableWeapon)
                        it.insertTable(
                            Table.toEntity(
                                table = table_,
                                weapon = weaponName
                            )
                        )
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(
                                {
                                    table.clear()
                                    table.addAll(tableWeapon)
                                    liveDatagetWeaponTable.postValue(table)
                                },
                                {

                                }
                            )
                }
            }
        }
    }
}