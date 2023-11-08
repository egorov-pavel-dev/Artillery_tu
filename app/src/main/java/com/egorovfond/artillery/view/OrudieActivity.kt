package com.egorovfond.artillery.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.OrudieRvAdapter
import kotlinx.android.synthetic.main.activity_orudie.*

class OrudieActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var adapter: OrudieRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orudie)

        addOrudie.setOnClickListener {
            presenter.addWeapon()
            updateList()
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

    private fun initRecyclerView() {

        adapter = OrudieRvAdapter()

        orudie_rv.layoutManager = LinearLayoutManager(this)
        orudie_rv.adapter = adapter
        orudie_rv.setHasFixedSize(true)
    }
}