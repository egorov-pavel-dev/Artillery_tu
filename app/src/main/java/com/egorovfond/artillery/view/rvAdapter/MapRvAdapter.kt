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
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallSessionStatus
import kotlinx.android.synthetic.main.card_map_list.view.*
import java.io.File
import kotlin.math.roundToInt


const val INSTALL_REQUEST_CODE = 123
class MapRvAdapter: RecyclerView.Adapter<MapRvAdapter.ViewHolder>(){
    private val presenter by lazy { Presenter.getPresenter() }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        private var splitInstallManager: SplitInstallManager? = null
        private val  globalSplitInstallManager: GlobalSplitInstallManager by lazy {
            GlobalSplitInstallManagerFactory.create(itemView.context)
        }

        private var mySessionId = 0

        fun getSplitInstallManager(name: String): SplitInstallManager{
            if (splitInstallManager == null) {
                //SplitInstallManagerFactory.create(itemView.context)
                val patt = getExternalFilesDirs(itemView.context, null)
                val path = "${patt[1]}/${name}.apk"
                splitInstallManager = FakeSplitInstallManagerFactory.createNewInstance(itemView.context, File(path))
                (splitInstallManager as FakeSplitInstallManager).setShouldNetworkError(true)
            }

            return splitInstallManager!!
        }

        fun bind(map: Map)= with(itemView) {
            map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
            //map.size = globalSplitInstallManager.getSessionState(globalSplitInstallManager.installedModules[map.name])

            map_rv_isloaded.setOnClickListener {
//                if (!map.isLoaded)
//                    loadMap(map)
//                else deleteMap(map)

                loadMapGlobal(map)
            }

            update(map)
        }

        private fun loadMapGlobal(map: Map) = with(itemView){
            val request = GlobalSplitInstallRequest.newBuilder()
                .addModule(map.name)
                .build()

            var mySessionId = 0

            globalSplitInstallManager.registerListener { state ->

                if (state.sessionId() == mySessionId) {
                    when (state.status()) {
                        GlobalSplitInstallSessionStatus.CANCELED -> {
                            Toast.makeText(itemView.context, "CANCELED", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.DOWNLOADING -> {
                            val progress =
                                (state.bytesDownloaded() * 100f / state.totalBytesToDownload()
                                    .toFloat()).roundToInt()
                            map.size = state.totalBytesToDownload().toFloat()

                            Toast.makeText(itemView.context, "DOWNLOADING $progress %", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.INSTALLING -> {
                            Toast.makeText(itemView.context, "INSTALLING", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.INSTALLED -> {
                            Toast.makeText(itemView.context, "INSTALLED", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.UNINSTALLED -> {
                            Toast.makeText(itemView.context, "UNINSTALLED", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.UNINSTALLING -> {
                            Toast.makeText(itemView.context, "UNINSTALLING", Toast.LENGTH_SHORT).show()
                        }

                        GlobalSplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                            globalSplitInstallManager.startConfirmationDialogForResult(
                                state,
                                this.context as Activity,
                                INSTALL_REQUEST_CODE
                            )
                        }
                    }
                }
            }

            if (globalSplitInstallManager.installedModules.contains(map.name)) {
                if(map.name == "base"){
                    globalSplitInstallManager.startInstall(request)
                }else {
                    globalSplitInstallManager.startUninstall(listOf(map.name))
                }
            } else {
                globalSplitInstallManager.startInstall(request)
            }.addOnSuccessListener { sessionId ->
                if (sessionId == 0) {
                    // Already installed
                    Toast.makeText(itemView.context, "¡SUCCESS!", Toast.LENGTH_SHORT).show()
                    map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                    update(map = map)
                } else {
                    mySessionId = sessionId
                    //map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                    //update(map = map)
                }
            }.addOnFailureListener {
                Toast.makeText(itemView.context, "¡FAILURE! ${it.message.toString()}", Toast.LENGTH_LONG).show()
                map.isLoaded = globalSplitInstallManager.installedModules.contains(map.name)
                update(map = map)
            }
        }

        private fun View.update(map: Map) {
            map_rv_name.setText(map.name)
            map_rv_size.setText("${Math.round(map.size/1024)} Kb")
            map_rv_isloaded.isChecked = map.isLoaded
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapRvAdapter.ViewHolder {
        return MapRvAdapter.ViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.card_map_list, parent, false) as View
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