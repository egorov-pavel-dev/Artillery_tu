package com.egorovfond.artillery.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.databinding.ActivityMapsBinding
import com.egorovfond.artillery.view.rvAdapter.MapRvAdapter
import com.jeppeman.globallydynamic.globalsplitinstall.GlobalSplitInstallConfirmResult

const val INSTALL_REQUEST_CODE = 123

class MapsActivity : AppCompatActivity() {
    private var adapter: MapRvAdapter? = null
    private lateinit var binding: ActivityMapsBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_maps)
        binding = ActivityMapsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

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