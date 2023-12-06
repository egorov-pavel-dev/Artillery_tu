package com.egorovfond.artillery.view

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.databinding.ActivityMapsBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.MapRvAdapter
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallSessionState
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallSessionStatus

const val INSTALL_REQUEST_CODE = 123
const val CHANNEL_ID = "Artillery"

class MapsActivity : AppCompatActivity() {
    private var adapter: MapRvAdapter? = null
    private lateinit var binding: ActivityMapsBinding

    private val observerUpdate = Observer<GlobalSplitInstallSessionState?> {state->
        if (state == null){
            Toast.makeText(this, "Ошибка при подключении к серверу", Toast.LENGTH_LONG).show()
        } else {
            when (state.status()) {
                GlobalSplitInstallSessionStatus.CANCELED -> {
                    Toast.makeText(this, "Загрузка отменена", Toast.LENGTH_LONG).show()
                }

                GlobalSplitInstallSessionStatus.DOWNLOADING -> {
                }

                GlobalSplitInstallSessionStatus.INSTALLING -> {
                    Toast.makeText(this, "Установка завершена", Toast.LENGTH_LONG).show()
                }

                GlobalSplitInstallSessionStatus.INSTALLED -> {

                }

                GlobalSplitInstallSessionStatus.UNINSTALLED -> {
                    Toast.makeText(this, "Удаление завершено", Toast.LENGTH_LONG).show()
                }

                GlobalSplitInstallSessionStatus.UNINSTALLING -> {

                }

                GlobalSplitInstallSessionStatus.REQUIRES_USER_CONFIRMATION -> {
                    Presenter.getSplitInstallManager(this)
                        .startConfirmationDialogForResult(
                            state,
                            this,
                            INSTALL_REQUEST_CODE
                        )
                }
            }
        }

        updateList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel.
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val mChannel = NotificationChannel(CHANNEL_ID, name, importance)
            mChannel.description = descriptionText
            val notificationManager = getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(mChannel)

            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(this, arrayOf(POST_NOTIFICATIONS), 1);
            }
        }
    }

    override fun onStart() {
        super.onStart()

        Presenter.subscribeServer().observeForever(observerUpdate)

        updateList()
    }

    override fun onStop() {
        super.onStop()

        Presenter.subscribeServer().removeObserver(observerUpdate)
    }

    private fun updateList() {
        adapter ?: initRecyclerView()
        adapter?.let {
            it.updateDataUser()
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initRecyclerView() {
        adapter = MapRvAdapter()

        binding.mapsRv.layoutManager = LinearLayoutManager(this)
        binding.mapsRv.adapter = adapter
        binding.mapsRv.setHasFixedSize(true)
    }
}