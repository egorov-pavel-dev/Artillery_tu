package com.egorovfond.artillery.view.rvAdapter

import android.annotation.SuppressLint
import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getExternalFilesDirs
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.math.Map
import com.egorovfond.artillery.presenter.Presenter
import com.google.android.play.core.splitinstall.SplitInstallManager
import com.google.android.play.core.splitinstall.SplitInstallRequest
import com.google.android.play.core.splitinstall.SplitInstallStateUpdatedListener
import com.google.android.play.core.splitinstall.model.SplitInstallSessionStatus
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManager
import com.google.android.play.core.splitinstall.testing.FakeSplitInstallManagerFactory
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
import kotlinx.android.synthetic.main.card_map_list.view.*
import java.io.File
import java.lang.Exception
import kotlin.math.roundToInt


const val INSTALL_REQUEST_CODE = 123
class MapRvAdapter: RecyclerView.Adapter<MapRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    private val mySessionId = mutableListOf<Int>()
    @Volatile private var obj = Any()

    class ViewHolder(itemView: View, mySession : MutableList<Int>, obj_:Any) :
        RecyclerView.ViewHolder(itemView) {
        @Volatile private var nameModule = ""
        @Volatile private var obj = obj_
        private val mySessionId = mySession

        private val  globalSplitInstallManager: GlobalSplitInstallManager by lazy {
            GlobalSplitInstallManagerFactory.create(itemView.context)
        }

        private lateinit var installUninstallrequest : GlobalSplitInstallTask<Int>

        fun bind(map: Map)= with(itemView) {
            map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)

            map_rv_isloaded.setOnClickListener {
                loadMapGlobal(map)
            }

            update(map)
        }

        private fun loadMapGlobal(map: Map) = with(itemView){
            //mySessionId.clear()
            if (mySessionId.size != 0){
                Toast.makeText(itemView.context, "Дождитесь окончания предыдущей установки", Toast.LENGTH_SHORT).show()
                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                update(map = map)
                return@with
            }

            map.size = 0f

            val onSuccessListener = object : OnGlobalSplitInstallSuccessListener<Int>{
                override fun onSuccess(sessionId: Int) {
                    if (sessionId == 0) {
                        // Already installed
                        Toast.makeText(itemView.context, "Карта уже установлена ", Toast.LENGTH_SHORT).show()
                    } else {
                        addItem(sessionId)
                    }
                }
            }
            val onCompleteListener = object : OnGlobalSplitInstallCompleteListener<Int>{
                override fun onComplete(task: GlobalSplitInstallTask<Int>?) {

                }
            }
            val onFailureListener = object : OnGlobalSplitInstallFailureListener{
                override fun onFailure(e: Exception?) {
                    Toast.makeText(itemView.context, "¡FAILURE! ${e!!.message.toString()}", Toast.LENGTH_LONG).show()
                    map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                    update(map = map)
                }

            }

            val listener = object : GlobalSplitInstallUpdatedListener {
                override fun onStateUpdate(state: GlobalSplitInstallSessionState?) {
                    if ( mySessionId.indexOf(state!!.sessionId()) != -1) {
                        when (state!!.status()) {
                            GlobalSplitInstallSessionStatus.CANCELED -> {
                                Toast.makeText(itemView.context, "CANCELED ${nameModule}", Toast.LENGTH_SHORT).show()
                            }

                            GlobalSplitInstallSessionStatus.DOWNLOADING -> {
                            }

                            GlobalSplitInstallSessionStatus.INSTALLING -> {
                                map.size = map.size + state.totalBytesToDownload().toFloat()
                                Toast.makeText(itemView.context, "INSTALLING ${nameModule}", Toast.LENGTH_SHORT).show()
                            }

                            GlobalSplitInstallSessionStatus.INSTALLED -> {
                                Toast.makeText(itemView.context, "INSTALLED ${nameModule}", Toast.LENGTH_SHORT).show()
                                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)

                                update(map = map)

                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLED -> {
                                Toast.makeText(itemView.context, "UNINSTALLED ${nameModule}", Toast.LENGTH_SHORT).show()
                                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                                update(map = map)
                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLING -> {
                                Toast.makeText(itemView.context, "UNINSTALLING ${nameModule}", Toast.LENGTH_SHORT).show()
                            }

                            GlobalSplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                                globalSplitInstallManager.startConfirmationDialogForResult(
                                    state,
                                    this@with.context as Activity,
                                    INSTALL_REQUEST_CODE
                                )
                            }
                        }
                    }
                }
            }

            if (nameModule.equals("")){
                nameModule = map.name
            }

            globalSplitInstallTask(listener)

            installUninstallrequest.addOnSuccessListener(onSuccessListener)
            installUninstallrequest.addOnCompleteListener(onCompleteListener)
            installUninstallrequest.addOnFailureListener(onFailureListener)
        }

        private fun globalSplitInstallTask(
            listener: GlobalSplitInstallUpdatedListener
        ){
            globalSplitInstallManager.registerListener(listener)

            installUninstallrequest =
                if (globalSplitInstallManager.installedModules.contains(nameModule)) {
                    if (nameModule.equals("altis")) {
                        val modules =
                            mutableListOf("altis")
                        if (globalSplitInstallManager.installedModules.contains("altis_part0")){
                            modules.add("altis_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("altis_part1")){
                            modules.add("altis_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("altis_part2")){
                            modules.add("altis_part2")
                        }
                        if (globalSplitInstallManager.installedModules.contains("altis_heightmap")){
                            modules.add("altis_heightmap")
                        }
                        globalSplitInstallManager.startUninstall(modules)
                    } else if (nameModule.equals("pecher")) {
                        val modules =
                            mutableListOf("pecher")
                        if (globalSplitInstallManager.installedModules.contains("pecher_part1")){
                            modules.add("pecher_part1")
                        }
                        globalSplitInstallManager.startUninstall(modules)
                    } else if (nameModule.equals("lythium")) {
                        val modules =
                            mutableListOf("lythium")
                        if (globalSplitInstallManager.installedModules.contains("lythium_part0")){
                            modules.add("lythium_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("lythium_part1")){
                            modules.add("lythium_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("lythium_part2")){
                            modules.add("lythium_part2")
                        }
                        if (globalSplitInstallManager.installedModules.contains("lythium_part3")){
                            modules.add("lythium_part3")
                        }
                        if (globalSplitInstallManager.installedModules.contains("lythium_part4")){
                            modules.add("lythium_part4")
                        }

                        globalSplitInstallManager.startUninstall(modules)
                    } else if (nameModule.equals("vt7")) {
                        val modules =
                            mutableListOf("vt7")
                        if (globalSplitInstallManager.installedModules.contains("vt7_part0")){
                            modules.add("vt7_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("vt7_part1")){
                            modules.add("vt7_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("vt7_part2")){
                            modules.add("vt7_part2")
                        }
                        globalSplitInstallManager.startUninstall(modules)
                    } else {
                        globalSplitInstallManager.startUninstall(listOf(nameModule))
                    }
                } else {
                    val request_par = GlobalSplitInstallRequest.newBuilder()
                        .addModule(nameModule)

                    if (nameModule.equals("altis")) {
                        request_par.addModule("altis_part0")
                        request_par.addModule("altis_part1")
                        request_par.addModule("altis_part2")
                        request_par.addModule("altis_heightmap")
                    } else if (nameModule.equals("pecher")) {
                        request_par.addModule("pecher_part1")

                    } else if (nameModule.equals("lythium")) {
                        request_par.addModule("lythium_part0")
                        request_par.addModule("lythium_part1")
                        request_par.addModule("lythium_part2")
                        request_par.addModule("lythium_part3")
                        request_par.addModule("lythium_part4")

                    } else if (nameModule.equals("vt7")) {
                        request_par.addModule("vt7_part0")
                        request_par.addModule("vt7_part1")
                        request_par.addModule("vt7_part2")
                    }

                    val request = request_par.build()
                    globalSplitInstallManager.startInstall(request)
                }
        }

        private fun View.update(map: Map) {
            clearItem()

            map_rv_name.setText(map.name)
            map_rv_size.setText("${Math.round(map.size/1024)} Kb")
            map_rv_isloaded.isChecked = map.isLoaded
        }

        fun addItem(session: Int){
            synchronized(obj){
                mySessionId.add(session)
            }
        }
        fun clearItem(){
            synchronized(obj){
                mySessionId.clear()
                nameModule = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRvAdapter.ViewHolder {
        return MapRvAdapter.ViewHolder(
            itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.card_map_list, parent, false) as View,
            mySession = mySessionId,
            obj_ = obj
        )
    }

    override fun onBindViewHolder(holder: MapRvAdapter.ViewHolder, position: Int) {
        holder.bind(presenter.maps[position])
    }

    override fun getItemCount(): Int {
        return presenter.maps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }

}