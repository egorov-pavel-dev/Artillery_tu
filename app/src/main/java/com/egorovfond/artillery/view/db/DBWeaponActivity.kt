package com.egorovfond.artillery.view.db

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.databinding.ActivityDbweaponBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.DBWeaponRvAdapter

class DBWeaponActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var adapter: DBWeaponRvAdapter? = null
    private lateinit var binding: ActivityDbweaponBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dbweapon)
        binding = ActivityDbweaponBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        binding.buttonDbweaponAdd.setOnClickListener {
            if (binding.weaponDbweaponName.text?.isEmpty() == false) {
                presenter.dbAddWeapon(
                    nameWeapon = binding.weaponDbweaponName.text.toString(),
                    mil = binding.weaponDbweaponMil.text.toString().toInt()
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

        binding.recyclerViewDbweaponWeapon.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewDbweaponWeapon.adapter = adapter
        binding.recyclerViewDbweaponWeapon.setHasFixedSize(true)
    }
}