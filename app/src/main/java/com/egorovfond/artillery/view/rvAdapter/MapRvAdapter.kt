package com.egorovfond.artillery.view.rvAdapter

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.RecyclerView
import com.egorovfond.artillery.R
import com.egorovfond.artillery.databinding.CardMapListBinding
import com.egorovfond.artillery.math.Map
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.CHANNEL_ID
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

const val INSTALL_REQUEST_CODE = 123

class MapRvAdapter: RecyclerView.Adapter<MapRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    private val mySessionId = 0
    @Volatile private var obj = Any()

    class ViewHolder(binding_: CardMapListBinding, mySession : Int, obj_:Any) :
        RecyclerView.ViewHolder(binding_.root) {
        val binding: CardMapListBinding = binding_
        @Volatile private var nameModule = ""
        @Volatile private var obj = obj_
        private var mySessionId = mySession

        private val  globalSplitInstallManager: GlobalSplitInstallManager by lazy {
            GlobalSplitInstallManagerFactory.create(binding.root.context)
        }

        private lateinit var installUninstallrequest : GlobalSplitInstallTask<Int>

        fun bind(map: Map, binding: CardMapListBinding)= with(itemView) {
            map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)

            binding.mapRvIsloaded.setOnClickListener {
                loadMapGlobal(map, binding)
            }

            update(map, binding)
        }

        private fun loadMapGlobal(map: Map, binding: CardMapListBinding) = with(itemView){
            //mySessionId.clear()
            if (mySessionId != 0){
                Toast.makeText(itemView.context, "Дождитесь окончания предыдущей установки", Toast.LENGTH_SHORT).show()
                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                update(map = map, binding)
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
                    //Toast.makeText(itemView.context, "¡FAILURE! ${e!!.message.toString()}", Toast.LENGTH_LONG).show()
                    map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                    update(map = map, binding)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        val builder =
                            NotificationCompat.Builder(this@ViewHolder.binding.root.context, CHANNEL_ID)
                                .setContentTitle("Загрузка карты")
                                .setContentText("Карта ${nameModule} удалена")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        with(NotificationManagerCompat.from(this@ViewHolder.itemView.context)) {
                            // notificationId is a unique int for each notification that you must define.
                            if (ActivityCompat.checkSelfPermission(
                                    this@ViewHolder.itemView.context,
                                    Manifest.permission.POST_NOTIFICATIONS
                                ) != PackageManager.PERMISSION_GRANTED
                            ) {
                                Toast.makeText(itemView.context, "¡FAILURE! ${e!!.message.toString()}", Toast.LENGTH_LONG).show()

                                return
                            }

                            notify(mySessionId, builder.build())
                        }
                    }else{
                        Toast.makeText(itemView.context, "¡FAILURE! ${e!!.message.toString()}", Toast.LENGTH_LONG).show()
                    }
                }

            }

            val listener = object : GlobalSplitInstallUpdatedListener {
                override fun onStateUpdate(state: GlobalSplitInstallSessionState?) {
                    if ( mySessionId == state!!.sessionId()) {
                        when (state!!.status()) {
                            GlobalSplitInstallSessionStatus.CANCELED -> {
                                //Toast.makeText(itemView.context, "CANCELED ${nameModule}", Toast.LENGTH_SHORT).show()
                            }

                            GlobalSplitInstallSessionStatus.DOWNLOADING -> {
                            }

                            GlobalSplitInstallSessionStatus.INSTALLING -> {
                                map.size = map.size + state.totalBytesToDownload().toFloat()
                                //Toast.makeText(itemView.context, "INSTALLING ${nameModule}", Toast.LENGTH_SHORT).show()
                            }

                            GlobalSplitInstallSessionStatus.INSTALLED -> {
                                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)

                                update(map = map, binding)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val builder =
                                        NotificationCompat.Builder(this@ViewHolder.binding.root.context, CHANNEL_ID)
                                            .setContentTitle("Загрузка карты")
                                            .setContentText("Карта ${nameModule} загружена и установлена")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    with(NotificationManagerCompat.from(this@ViewHolder.itemView.context)) {
                                        // notificationId is a unique int for each notification that you must define.
                                        if (ActivityCompat.checkSelfPermission(
                                                this@ViewHolder.itemView.context,
                                                Manifest.permission.POST_NOTIFICATIONS
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            Toast.makeText(this@ViewHolder.binding.root.context, "Карта ${nameModule} загружена и установлена", Toast.LENGTH_SHORT).show()

                                            return
                                        }

                                        notify(mySessionId, builder.build())
                                    }
                                }else{
                                    Toast.makeText(this@ViewHolder.binding.root.context, "Карта ${nameModule} загружена и установлена", Toast.LENGTH_SHORT).show()
                                }

                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLED -> {
                                //Toast.makeText(itemView.context, "UNINSTALLED ${nameModule}", Toast.LENGTH_SHORT).show()
                                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                                update(map = map, binding)

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                    val builder =
                                        NotificationCompat.Builder(this@ViewHolder.binding.root.context, CHANNEL_ID)
                                            .setContentTitle("Загрузка карты")
                                            .setContentText("Карта ${nameModule} удалена")
                                            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                    with(NotificationManagerCompat.from(this@ViewHolder.itemView.context)) {
                                        // notificationId is a unique int for each notification that you must define.
                                        if (ActivityCompat.checkSelfPermission(
                                                this@ViewHolder.itemView.context,
                                                Manifest.permission.POST_NOTIFICATIONS
                                            ) != PackageManager.PERMISSION_GRANTED
                                        ) {
                                            Toast.makeText(this@ViewHolder.binding.root.context, "Карта ${nameModule} удалена", Toast.LENGTH_SHORT).show()

                                            return
                                        }

                                        notify(mySessionId, builder.build())
                                    }
                                }else{
                                    Toast.makeText(this@ViewHolder.binding.root.context, "Карта ${nameModule} удалена", Toast.LENGTH_SHORT).show()
                                }
                            }

                            GlobalSplitInstallSessionStatus.UNINSTALLING -> {
                                //Toast.makeText(itemView.context, "UNINSTALLING ${nameModule}", Toast.LENGTH_SHORT).show()
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
                    val modules =
                        mutableListOf(nameModule)
                    if (nameModule.equals("altis")) {
                        if (globalSplitInstallManager.installedModules.contains("altis_part0")){
                            modules.add("altis_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("altis_part1")){
                            modules.add("altis_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("altis_part2")){
                            modules.add("altis_part2")
                        }
                    } else if (nameModule.equals("vt7")) {
                        if (globalSplitInstallManager.installedModules.contains("vt7_part0")){
                            modules.add("vt7_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("vt7_part1")){
                            modules.add("vt7_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("vt7_part2")){
                            modules.add("vt7_part2")
                        }
                    } else if (nameModule.equals("takistan")) {
                        if (globalSplitInstallManager.installedModules.contains("takistan_part0")){
                            modules.add("takistan_part0")
                        }
                        if (globalSplitInstallManager.installedModules.contains("takistan_part1")){
                            modules.add("takistan_part1")
                        }
                        if (globalSplitInstallManager.installedModules.contains("takistan_part2")){
                            modules.add("takistan_part2")
                        }
                    }

//                    if (globalSplitInstallManager.installedModules.contains("${nameModule}_heightmap")){
//                        modules.add("${nameModule}_heightmap")
//                    }

                    globalSplitInstallManager.startUninstall(modules)
                } else {
                    val request_par = GlobalSplitInstallRequest.newBuilder()
                        .addModule(nameModule)
                    if (nameModule.equals("altis")) {
                        request_par.addModule("altis_part0")
                        request_par.addModule("altis_part1")
                        request_par.addModule("altis_part2")
                    } else if (nameModule.equals("vt7")) {
                        request_par.addModule("vt7_part0")
                        request_par.addModule("vt7_part1")
                        request_par.addModule("vt7_part2")
                    } else if (nameModule.equals("takistan")) {
                        request_par.addModule("takistan_part0")
                        request_par.addModule("takistan_part1")
                        request_par.addModule("takistan_part2")
                    }

                    //request_par.addModule("${nameModule}_heightmap")

                    val request = request_par.build()
                    globalSplitInstallManager.startInstall(request)
                }
        }

        private fun update(map: Map, binding: CardMapListBinding) {
            clearItem()

            binding.mapRvName.setText(map.name)
            binding.mapRvSize.setText("${Math.round(map.size/1024)} Kb")
            binding.mapRvIsloaded.isChecked = map.isLoaded
        }

        fun addItem(session: Int){
            synchronized(obj){
                mySessionId = session
            }
        }
        fun clearItem(){
            synchronized(obj){
                mySessionId = 0
                nameModule = ""
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRvAdapter.ViewHolder {

        return ViewHolder(
//            itemView = LayoutInflater.from(parent.context)
//                .inflate(R.layout.card_map_list, parent, false) as View,
            binding_ = CardMapListBinding.inflate(LayoutInflater.from(parent.context), parent, false),
            mySession = mySessionId,
            obj_ = obj
        )
    }

    override fun onBindViewHolder(holder: MapRvAdapter.ViewHolder, position: Int) {
        holder.bind(presenter.maps[position], holder.binding)
    }

    override fun getItemCount(): Int {
        return presenter.maps.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateDataUser() {
        notifyDataSetChanged()
    }

}