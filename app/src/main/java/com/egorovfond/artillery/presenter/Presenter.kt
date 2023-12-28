package com.egorovfond.artillery.presenter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.egorovfond.artillery.data.Enemy
import com.egorovfond.artillery.data.Orudie
import com.egorovfond.artillery.data.Popravki
import com.egorovfond.artillery.data.Result
import com.egorovfond.artillery.data.localTable.HeightMaps
import com.egorovfond.artillery.database.DB
import com.egorovfond.artillery.database.room.entity.WeaponEntity
import com.egorovfond.artillery.math.Artilery
import com.egorovfond.artillery.math.Azimut
import com.egorovfond.artillery.math.Map
import com.egorovfond.artillery.math.Table
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallManager
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallManagerFactory
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallRequest
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallSessionState
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallSessionStatus
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallUpdatedListener
import com.jeppeman.globallydynamic.tasks.GlobalSplitInstallTask
import com.jeppeman.globallydynamic.tasks.OnGlobalSplitInstallCompleteListener
import com.jeppeman.globallydynamic.tasks.OnGlobalSplitInstallFailureListener
import com.jeppeman.globallydynamic.tasks.OnGlobalSplitInstallSuccessListener

const val BULLET_NOTHING = "Не выбран"

class Presenter: ViewModel() {
    var localmap = false

    private val TAG = "PRESENTER"
    @Volatile private var orudie_list = mutableListOf<Orudie>()
    @Volatile private var target_list = mutableListOf<Enemy>()
    private val popravki = Popravki()
    @Volatile private var currentWeapon: Orudie = Orudie("Нет данных")
    @Volatile var url = ""
    @Volatile var map_settings = 0
    @Volatile var currentEnemy: Enemy = Enemy("Нет данных")
    @Volatile var currentTable = mutableListOf<Table>()
    @Volatile var weaponlist = mutableListOf<WeaponEntity>()
    val heightMap = HeightMaps()
    var autoupdate = true

    val maps = mutableListOf(
        Map(name = "tableweapon", url = "com.egorovfond.tableweapon", size = 0f, isLoaded = false),
        Map(name = "altis", url = "com.egorovfond.altis", size = 0f, isLoaded = false),
        Map(name = "brf_sumava", url = "com.egorovfond.brf_sumava", size = 0f, isLoaded = false),
        Map(name = "chernarus", url = "com.egorovfond.chernarus", size = 0f, isLoaded = false),
        Map(name = "chongo", url = "com.egorovfond.chongo", size = 0f, isLoaded = false),
        Map(name = "dingor", url = "com.egorovfond.dingor", size = 0f, isLoaded = false),
        Map(name = "eden", url = "com.egorovfond.eden", size = 0f, isLoaded = false),
        Map(name = "enoch", url = "com.egorovfond.enoch", size = 0f, isLoaded = false),
        Map(name = "fallujahint", url = "com.egorovfond.fallujahint", size = 0f, isLoaded = false),
        Map(name = "kujari", url = "com.egorovfond.kujari", size = 0f, isLoaded = false),
        Map(name = "lythium", url = "com.egorovfond.lythium", size = 0f, isLoaded = false),
        Map(name = "malden", url = "com.egorovfond.malden", size = 0f, isLoaded = false),
        Map(name = "mcn_aliabad", url = "com.egorovfond.mcn_aliabad", size = 0f, isLoaded = false),
        Map(name = "mountains_acr", url = "com.egorovfond.mountains_acr", size = 0f, isLoaded = false),
        Map(name = "napf", url = "com.egorovfond.napf", size = 0f, isLoaded = false),
        Map(name = "pabst_yellowstone", url = "com.egorovfond.pabst_yellowstone", size = 0f, isLoaded = false),
        Map(name = "panthera3", url = "com.egorovfond.panthera3", size = 0f, isLoaded = false),
        Map(name = "pecher", url = "com.egorovfond.pecher", size = 0f, isLoaded = false),
        Map(name = "pja310", url = "com.egorovfond.pja310", size = 0f, isLoaded = false),
        Map(name = "pja314", url = "com.egorovfond.pja314", size = 0f, isLoaded = false),
        Map(name = "ruha", url = "com.egorovfond.ruha", size = 0f, isLoaded = false),
        Map(name = "sara", url = "com.egorovfond.sara", size = 0f, isLoaded = false),
        Map(name = "sara_dbe1", url = "com.egorovfond.sara_dbe1", size = 0f, isLoaded = false),
        Map(name = "seangola", url = "com.egorovfond.seangola", size = 0f, isLoaded = false),
        Map(name = "stratis", url = "com.egorovfond.stratis", size = 0f, isLoaded = false),
        Map(name = "takistan", url = "com.egorovfond.takistan", size = 207f, isLoaded = false),
        Map(name = "tanoa", url = "com.egorovfond.tanoa", size = 0f, isLoaded = false),
        Map(name = "taunus", url = "com.egorovfond.taunus", size = 0f, isLoaded = false),
        Map(name = "tem_anizay", url = "com.egorovfond.tem_anizay", size = 0f, isLoaded = false),
        Map(name = "tem_suursaariv", url = "com.egorovfond.tem_suursaariv", size = 0f, isLoaded = false),
        Map(name = "uzbin", url = "com.egorovfond.uzbin", size = 0f, isLoaded = false),
        Map(name = "vt7", url = "com.egorovfond.vt7", size = 0f, isLoaded = false),
        Map(name = "wl_rosche", url = "com.egorovfond.wl_rosche", size = 0f, isLoaded = false),
        Map(name = "woodland_acr", url = "com.egorovfond.woodland_acr", size = 0f, isLoaded = false),
        Map(name = "zargabad", url = "com.egorovfond.zargabad", size = 0f, isLoaded = false)
    )

    private val liveDatagetWeaponList: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val liveDatagetWeaponTable: MutableLiveData<MutableList<Table>> = MutableLiveData()
    private val liveDataUpdateResult: MutableLiveData<Boolean> = MutableLiveData()
    private val liveDatagetBullet: MutableLiveData<MutableList<String>> = MutableLiveData()
    private val liveDatagetBase: MutableLiveData<MutableList<String>> = MutableLiveData()
    fun subscribegetWeaponList(): MutableLiveData<MutableList<String>> = liveDatagetWeaponList
    fun subscribegetWeaponTable(): MutableLiveData<MutableList<Table>> = liveDatagetWeaponTable
    fun subscribeUpdateResult(): MutableLiveData<Boolean> = liveDataUpdateResult
    fun subscribegetBullet(): MutableLiveData<MutableList<String>> = liveDatagetBullet
    fun subscribegetBase(): MutableLiveData<MutableList<String>> = liveDatagetBase

    private val observergetWeaponList = Observer<MutableList<WeaponEntity>> {
        weaponlist.clear()
        weaponlist.addAll(it)

        val weapon = mutableListOf<String>()
        for (elem in it) weapon.add(elem.name)
        liveDatagetWeaponList.postValue(weapon)
    }
    private val observergetWeaponTable = Observer<MutableList<Table>> {
        currentTable.clear()
        currentTable.addAll(it)

        liveDatagetWeaponTable.postValue(it)
    }
    private val observerUpdateResult = Observer<Boolean> {
        liveDataUpdateResult.postValue(it)
    }
    private val observergetBullet = Observer<MutableList<String>> {
        liveDatagetBullet.postValue(it)
    }
    private val observergetBase = Observer<MutableList<String>> {
        liveDatagetBase.postValue(it)
    }
    private val observerInitDB = Observer<Boolean> {
        if (it) DB.firstLoad()
    }
    companion object {
        private var presenter: Presenter? = null
        @Volatile private var globalSplitInstallManager: GlobalSplitInstallManager? = null
        private val mapsLoad = mutableListOf<String>()
        private val sessions = mutableListOf<Int>()
        private val liveDataUpate: MutableLiveData<GlobalSplitInstallSessionState?> = MutableLiveData()
        private lateinit var listener: GlobalSplitInstallUpdatedListener

        fun subscribeServer(): MutableLiveData<GlobalSplitInstallSessionState?> = liveDataUpate

        fun getPresenter(): Presenter {
            if (presenter == null){
                synchronized(Presenter::class) {
                    presenter = Presenter()
                }
            }

            return this.presenter!!
        }

        fun getSplitInstallManager(context: Context): GlobalSplitInstallManager{
            if (globalSplitInstallManager == null){
                synchronized(Presenter::class) {
                    globalSplitInstallManager = GlobalSplitInstallManagerFactory.create(context)
                }
            }

            return this.globalSplitInstallManager!!
        }

        fun loadMap(mapName:String){
            addMapsLoad(mapName)

            var mySessionId = 0
            listener =
                GlobalSplitInstallUpdatedListener { state ->
                    if (sessions.find { it == state!!.sessionId() } != null) {
                        when (state!!.status()) {
                            GlobalSplitInstallSessionStatus.CANCELED -> {
                                removeMapsLoad()
                                sessions.clear()

                                liveDataUpate.postValue(state)
                            }

                            GlobalSplitInstallSessionStatus.DOWNLOADING -> {
                            }

                            GlobalSplitInstallSessionStatus.INSTALLING -> {
                                getPresenter().maps.find { it.name == mapName }?.let {
                                    it.size = state.totalBytesToDownload().toFloat()
                                }
                            }

                            GlobalSplitInstallSessionStatus.INSTALLED -> {
                                getPresenter().maps.find { it.name == mapName }?.let { map ->
                                    globalSplitInstallManager?.let {
                                        map.isLoaded = it.installedModules.contains(mapName)
                                    }
                                }
                                removeMapsLoad()
                                sessions.clear()

                                liveDataUpate.postValue(state)
                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLED -> {
                                getPresenter().maps.find { it.name == mapName }?.let { map ->
                                    globalSplitInstallManager?.let {
                                        map.isLoaded = it.installedModules.contains(mapName)
                                    }
                                }
                                removeMapsLoad()
                                sessions.clear()

                                liveDataUpate.postValue(state)
                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLING -> {

                            }

                            GlobalSplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                                removeMapsLoad()
                                sessions.clear()

                                liveDataUpate.postValue(state)
                            }
                        }
                    }
                }
            val onCompleteListener = OnGlobalSplitInstallCompleteListener<Int> { }
            val onFailureListener = OnGlobalSplitInstallFailureListener {
                removeMapsLoad()
                sessions.remove(mySessionId)

                globalSplitInstallManager?.unregisterListener(listener)
                liveDataUpate.postValue(null)
            }
            val onSuccessListener =
                OnGlobalSplitInstallSuccessListener<Int> { sessionId ->
                    if (sessionId != 0) {
                        sessions.add(sessionId)
                        mySessionId = sessionId
                    }
                }

            globalSplitInstallManager?.let {
                it.registerListener(listener)

                val installUninstallrequest =
                    if (it.installedModules.contains(mapName)) {
                        val modules =
                            mutableListOf(mapName)
                        if (mapName.equals("altis")) {
                            if (it.installedModules.contains("altis_part0")){
                                modules.add("altis_part0")
                            }
                            if (it.installedModules.contains("altis_part1")){
                                modules.add("altis_part1")
                            }
                            if (it.installedModules.contains("altis_part2")){
                                modules.add("altis_part2")
                            }
                        } else if (mapName.equals("vt7")) {
                            if (it.installedModules.contains("vt7_part0")){
                                modules.add("vt7_part0")
                            }
                            if (it.installedModules.contains("vt7_part1")){
                                modules.add("vt7_part1")
                            }
                            if (it.installedModules.contains("vt7_part2")){
                                modules.add("vt7_part2")
                            }
                        } else if (mapName.equals("takistan")) {
                            if (it.installedModules.contains("takistan_part0")){
                                modules.add("takistan_part0")
                            }
                            if (it.installedModules.contains("takistan_part1")){
                                modules.add("takistan_part1")
                            }
                            if (it.installedModules.contains("takistan_part2")){
                                modules.add("takistan_part2")
                            }
                        }

                        it.startUninstall(modules)
                    } else {
                        val request_par = GlobalSplitInstallRequest.newBuilder()
                            .addModule(mapName)
                        if (mapName.equals("altis")) {
                            request_par.addModule("altis_part0")
                            request_par.addModule("altis_part1")
                            request_par.addModule("altis_part2")
                        } else if (mapName.equals("vt7")) {
                            request_par.addModule("vt7_part0")
                            request_par.addModule("vt7_part1")
                            request_par.addModule("vt7_part2")
                        } else if (mapName.equals("takistan")) {
                            request_par.addModule("takistan_part0")
                            request_par.addModule("takistan_part1")
                            request_par.addModule("takistan_part2")
                        }

                        //request_par.addModule("${nameModule}_heightmap")

                        val request = request_par.build()
                        it.startInstall(request)
                    }

                installUninstallrequest.addOnSuccessListener(onSuccessListener)
                installUninstallrequest.addOnCompleteListener(onCompleteListener)
                installUninstallrequest.addOnFailureListener(onFailureListener)
            }
        }
        fun getMapsLoad(): MutableList<String>{
            return mapsLoad
        }

        fun addMapsLoad(item: String){
            synchronized(Presenter::class) {
                mapsLoad.add(item)
            }
        }

        fun removeMapsLoad(){
            synchronized(Presenter::class) {
                //mapsLoad.remove(item)
                mapsLoad.clear()

                if (mapsLoad.size == 0) globalSplitInstallManager?.unregisterListener(listener)
            }
        }
    }

    init {
        DB.subscribegetWeaponList().observeForever(observergetWeaponList)
        DB.subscribegetWeaponTable().observeForever(observergetWeaponTable)
        DB.subscribeUpdateResult().observeForever(observerUpdateResult)
        DB.subscribegetBullet().observeForever(observergetBullet)
        DB.subscribegetBase().observeForever(observergetBase)
        DB.subscribeInicialize().observeForever(observerInitDB)

        getWeaponFromDB()
    }

    override fun onCleared() {
        super.onCleared()

        DB.subscribegetWeaponList().removeObserver(observergetWeaponList)
        DB.subscribegetWeaponTable().removeObserver(observergetWeaponTable)
        DB.subscribeUpdateResult().removeObserver(observerUpdateResult)
        DB.subscribegetBullet().removeObserver(observergetBullet)
        DB.subscribegetBullet().removeObserver(observergetBase)
        DB.subscribeInicialize().removeObserver(observerInitDB)
    }

    /// Маин Активикти
    fun addTarget(){
        synchronized(Presenter::class) {
            val enemy = Enemy(
                nameTarget = "Цель ${target_list.size}",
                position = target_list.size
            )
            target_list.add(enemy)

            addUpdateResultByTarget(enemy)
        }
    }

    /// Поправки
    fun onClick_Save_Popravki(windSpeed: Float, windUgol: Int, humidity: Float, temp: Float, radius: Int){
        this.popravki.humidity = humidity
        this.popravki.temp = temp
        this.popravki.wind_cross = windUgol
        this.popravki.wind_speed = windSpeed
        this.popravki.radius = radius
        //updateTargetList()
    }
    fun getPopravki() = popravki

    /// Орудие
    fun addWeapon(){
        val weapon = Orudie(nameOrudie = "Орудие ${orudie_list.size}", position = orudie_list.size)
        orudie_list.add(weapon)
        addUpdateResultByWeapon(weapon)
    }

    private fun updateTargetList(target: Enemy) {
        synchronized(Presenter::class) {
            for (result in target.result) {
                result.bullet = "...Обновляется..."
                if (result.azimut_target == 0) updateResult(result_ = result, target = target)

                DB.updateTargetList(target, result)
            }
        }
    }

    fun getWeapon(): MutableList<Orudie> {
        return this.orudie_list
    }
    fun setCurrentWeapon(weapon: Orudie) {
        synchronized(Presenter::class) {
            this.currentWeapon = weapon
        }
    }

    fun getCurrentWeapon() = this.currentWeapon
    fun setCoordinate(x: Float, y: Float) {
        synchronized(Presenter::class) {
            if (map_settings == 1) {
                map_settings = 0

                currentWeapon.x = x
                currentWeapon.y = y

                orudie_list[currentWeapon.position].x = x
                orudie_list[currentWeapon.position].y = y

                //updateOrudieByXY(orudie_list[currentWeapon.position])
            }
            if (map_settings == 2) {
                map_settings = 0

                currentWeapon.x_Dot = x
                currentWeapon.y_Dot = y

                orudie_list[currentWeapon.position].x_Dot = x
                orudie_list[currentWeapon.position].y_Dot = y

                //updateOrudieByXY(orudie_list[currentWeapon.position])
            }
            if (map_settings == 3) {
                map_settings = 0

                currentEnemy.x = x
                currentEnemy.y = y

                target_list[currentEnemy.position].x = x
                target_list[currentEnemy.position].y = y

                //updateTargetByXY(target_list[currentEnemy.position])
                //updateTargetList(target_list[currentEnemy.position])
            }
            if (map_settings == 4) {
                map_settings = 0

                currentEnemy.x_prilet = x
                currentEnemy.y_prilet = y

                target_list[currentEnemy.position].x_prilet = x
                target_list[currentEnemy.position].y_prilet = y

                //updateTargetByXY(target_list[currentEnemy.position])
                //updateTargetList(target_list[currentEnemy.position])
            }
        }
    }

    fun setAzimutDot(orudie: Orudie, azimut: Int){
        //synchronized(Presenter::class) {
            orudie.azimut_Dot = azimut
        //}
    }

    fun setWeaponIntoOrudie(weapon: String){
        synchronized(Presenter::class) {
            this.currentWeapon.weapon = weapon
            orudie_list[currentWeapon.position].weapon = weapon

            this.getCurrentWeapon().mil = getMil(weapon)
        }
    }

    fun setBulletIntoOrudie(bullet: String){
        synchronized(Presenter::class) {
            this.currentWeapon.bullet = bullet
            orudie_list[currentWeapon.position].bullet = bullet
        }
    }
    fun setBaseIntoOrudie(base: String){
        synchronized(Presenter::class) {
            this.currentWeapon.base = base
            orudie_list[currentWeapon.position].base = base
        }
    }

    fun getMil(weapon: String): Int {
        var mil = 6000
        for(i in weaponlist) {
            if (i.name.equals(weapon)) {
                mil = i.mil
                break
            }
        }
        return  mil
    }

    fun getWeaponFromDB() {
        DB.getWeaponList()
    }

    fun initBullet(weaponName: String, tableWeapon: MutableList<Table>){
        DB.initBullet(weaponName, tableWeapon)
    }
    fun updateOrudieByXY(weapon: Orudie){
        synchronized(Presenter::class) {
            val range = Azimut.getRangeByCoordinat(
                weapon.x,
                weapon.y,
                weapon.x_Dot,
                weapon.y_Dot
            )
            setAzimutDot(
                weapon,
                Azimut.convertCoordinatToAzimut(
                    weapon.x,
                    weapon.y,
                    weapon.x_Dot,
                    weapon.y_Dot,
                    range,
                    weapon.mil
                ).toInt()
            )
        }
    }

    private fun updateResult(
        result_: Result,
        target: Enemy
    ) {
        synchronized(Presenter::class) {
            result_.distace = Azimut.getRangeByCoordinat(
                target.x,
                target.y,
                result_.orudie.x,
                result_.orudie.y
            )
            result_.azimut_target =
                Azimut.convertCoordinatToAzimut(
                    result_.orudie.x,
                    result_.orudie.y,
                    target.x,
                    target.y,
                    result_.distace,
                    result_.orudie.mil
                ).toInt()

            result_.ugol =
                Azimut.getBussolUgol(
                    ugolTH = result_.orudie.azimut_Dot,
                    ugolTarget = result_.azimut_target,
                    mil = result_.orudie.mil
                )
        }
    }

    fun updateTHbyAzimut(weapon: Orudie) {
        synchronized(Presenter::class) {
            var range = 100f
            if (weapon.x_Dot != 0f) {
                range = Azimut.getRangeByCoordinat(
                    weapon.x,
                    weapon.y,
                    weapon.x_Dot,
                    weapon.y_Dot
                )
            }

            weapon.x_Dot = Azimut.getXCoordinat(
                weapon.x,
                range,
                weapon.azimut_Dot,
                weapon.mil
            )
            weapon.y_Dot = Azimut.getYCoordinat(
                weapon.y,
                range,
                weapon.azimut_Dot,
                weapon.mil
            )
        }
    }

    fun getMaxRange(isEnemy: Boolean): Int{
        if (isEnemy) return Artilery.getMaxRange(currentTable, getCurrentWeapon().bullet, getCurrentWeapon().mortir)
        return Artilery.getMaxRange(currentTable, getCurrentWeapon().bullet, null)
    }

    fun getMinRange(isEnemy: Boolean): Int{
        if (isEnemy) return Artilery.getMinRange(currentTable, getCurrentWeapon().bullet, getCurrentWeapon().mortir)
        return Artilery.getMinRange(currentTable, getCurrentWeapon().bullet, null)
    }

    private fun addUpdateResultByWeapon(weapon: Orudie) {
        synchronized(Presenter::class) {
            for (target in target_list) {
                val newResult = Result(orudie = weapon)
                target.result.add(newResult)

            }
        }
    }

    private fun addUpdateResultByTarget(target: Enemy) {
        synchronized(Presenter::class) {
            for (weapon in orudie_list) {
                val newResult = Result(orudie = weapon)
                target.result.add(newResult)

                DB.updateTargetList(target = target, result = newResult)
            }
        }
    }

    fun getTargetList(): MutableList<Enemy> {
        return target_list
    }
    fun getResultAbout(): MutableList<Result> {
        return target_list[currentEnemy.position].result
    }
    fun changeMortirResultAbout(position: Int, checked: Boolean) {
        synchronized(Presenter::class) {
            try {
                target_list[currentEnemy.position].result[position].orudie.mortir = checked
            } catch (e: Throwable) {
                Log.d(TAG, "changeMortirResultAbout: $e")
            }
        }
    }
    fun updateWeaponTH(notBussol: Boolean, result: Result) {
        synchronized(Presenter::class) {
            try {
                val enemy = target_list[currentEnemy.position]
                if (enemy.x != 0f || enemy.y != 0f) {
                    result.distace = Azimut.getRangeByCoordinat(
                        result.orudie.x,
                        result.orudie.y,
                        enemy.x,
                        enemy.y
                    )
                    result.azimut_target =
                        Azimut.convertCoordinatToAzimut(
                            result.orudie.x,
                            result.orudie.y,
                            enemy.x,
                            enemy.y,
                            result.distace,
                            result.orudie.mil
                        ).toInt()
                }
                // Проверка направления!

                if (notBussol) {
                    result.ugol =
                        Azimut.getBussolUgol(
                            ugolTH = result.orudie.azimut_Dot,
                            ugolTarget = result.azimut_target,
                            mil = result.orudie.mil
                        )
                }

                //updateTHbyAzimut(result.orudie)

                updateTargetList(target_list[currentEnemy.position])

            } catch (e: Throwable) {
                Log.d(TAG, "updateWeaponTH: $e")
            } finally {

            }
        }
    }

    fun dbAddWeapon(nameWeapon: String, mil: Int) {
        DB.addWeapon(nameWeapon, mil = mil)
    }
    fun getWeaponTableFromDB(weaponID: String, table: MutableList<Table>) {
        DB.getWeaponTable(weaponID, table)
    }
    fun getWeaponBullet(weaponID: String, base: String){
        DB.getWeaponBullet(weapon = weaponID, base = base)
    }
    fun getWeaponBase(weaponID: String){
        DB.getWeaponBase(weapon = weaponID)
    }
    fun updateTableWeapoon(weaponID: String, baseID: String, bulletID: String) {
        DB.updateTableWeapoon(weapon = weaponID, bullet = bulletID, base = baseID)
    }
    fun saveWeaponTableIntoDB(weapon: String){
        DB.saveWeaponTable(currentTable, weapon = weapon)
    }

    fun updateDot(notBussol: Boolean, result: Result) {
        if (notBussol) {
//            result.ugol =
//                Azimut.getBussolUgol(
//                    ugolTH = result.orudie.azimut_Dot,
//                    ugolTarget = result.azimut_target,
//                    mil = result.orudie.mil
//                )
//
//            updateTHAndXY(
//                result,
//                this.getTargetList()[this.currentEnemy.position].x,
//                this.getTargetList()[this.currentEnemy.position].y
//            )
        }else {
            setAzimutDot(
                result.orudie,
                Azimut.getAzimutByTHHaveBussol(
                    result.ugol,
                    result.azimut_target,
                    result.orudie.mil
                )
            )
        }
        updateTHbyAzimut(result.orudie)
    }
    fun updateDotPrilet(result: Result) {
        Artilery.cor_getAim(weapon = result.orudie, enemy = currentEnemy, result = result)
        Artilery.cor_getCorner(weapon = result.orudie, enemy = currentEnemy, result = result)
    }

    fun initializeDB() = DB.firstLoad()
    fun reset(){
        DB.dropTables()
    }
    fun initWeapon(weaponID: String) = DB.initWeapon(weaponName = weaponID)

    fun getHeight(bmpOriginal: Bitmap, x_: Float, y_: Float): Int {
        val mapHeight = heightMap

        val part = (Math.round((x_ * 1000)) / mapHeight.part)


        // get one pixel color
        val x = Math.round((x_ * 1000)) - (part * mapHeight.part)
        val y = Math.round((y_ * 1000))

        val pixel = bmpOriginal.getPixel(x,y)
        val red = Color.red(pixel)

        var h = Math.round((red.toFloat() / 255) * (heightMap.maxHeight - heightMap.minHeight))

        if (h < 0) h = 0
        return h
    }
}