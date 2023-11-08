package com.egorovfond.artillery.view.db

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.DBWeaponRvAdapter
import kotlinx.android.synthetic.main.activity_dbweapon.*

class DBWeaponActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var adapter: DBWeaponRvAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbweapon)

        button_dbweapon_add.setOnClickListener {
            if (weapon_dbweapon_name.text?.isEmpty() == false) {
                presenter.dbAddWeapon(
                    nameWeapon = weapon_dbweapon_name.text.toString(),
                    mil = weapon_dbweapon_mil.text.toString().toInt()
                )
            }

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

        adapter = DBWeaponRvAdapter()

        recyclerView_dbweapon_weapon.layoutManager = LinearLayoutManager(this)
        recyclerView_dbweapon_weapon.adapter = adapter
        recyclerView_dbweapon_weapon.setHasFixedSize(true)
    }
}