package com.egorovfond.artillery.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.view.rvAdapter.MapRvAdapter
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallConfirmResult
import kotlinx.android.synthetic.main.activity_maps.*
import com.github.javiersantos.appupdater.AppUpdater
import com.github.javiersantos.appupdater.enums.Display


const val INSTALL_REQUEST_CODE = 123
class MapsActivity : AppCompatActivity() {
    private var adapter: MapRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        btn_maps_update.setOnClickListener {
            val appUpdater = AppUpdater(this)
                .setUpdateXML("https://github.com/egorov-pavel-dev/Artillery_tu/blob/master/app/version.xml")
                .setDisplay(Display.SNACKBAR)
                .showAppUpdated(true)
                .start()
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

        maps_rv.layoutManager = LinearLayoutManager(this)
        maps_rv.adapter = adapter
        maps_rv.setHasFixedSize(true)
    }
}