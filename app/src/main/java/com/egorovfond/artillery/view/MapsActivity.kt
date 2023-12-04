package com.egorovfond.artillery.view

import android.Manifest
import android.Manifest.permission.POST_NOTIFICATIONS
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.databinding.ActivityMapsBinding
import com.egorovfond.artillery.view.rvAdapter.MapRvAdapter
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallConfirmResult

const val INSTALL_REQUEST_CODE = 123
const val CHANNEL_ID = "Artillery"

class MapsActivity : AppCompatActivity() {
    private var adapter: MapRvAdapter? = null
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maps)
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

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == 1){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            }
        }
    }
    override fun onStart() {
        super.onStart()

        updateList()
    }

    private fun updateList() {
        adapter ?: initRecyclerView()
        adapter?.let {
            it.updateDataUser()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == INSTALL_REQUEST_CODE
            && data?.hasExtra(GlobalSplitInstallConfirmResult.EXTRA_RESULT) == true
        ) {
            val confirmResult = data.getIntExtra(
                GlobalSplitInstallConfirmResult.EXTRA_RESULT,
                GlobalSplitInstallConfirmResult.RESULT_DENIED
            )

            if (confirmResult == GlobalSplitInstallConfirmResult.RESULT_CONFIRMED) {
                // User granted permission, install again!
            }
        }
    }

    private fun initRecyclerView() {

        adapter = MapRvAdapter()

        binding.mapsRv.layoutManager = LinearLayoutManager(this)
        binding.mapsRv.adapter = adapter
        binding.mapsRv.setHasFixedSize(true)
    }
}