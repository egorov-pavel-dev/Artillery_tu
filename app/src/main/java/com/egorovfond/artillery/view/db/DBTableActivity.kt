package com.egorovfond.artillery.view.db

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.math.Table
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.DBTableRvAdapter
import kotlinx.android.synthetic.main.activity_dbtable.*

class DBTableActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private val weapon = mutableListOf<String>()
    private val bullet = mutableListOf<String>()
    private val base = mutableListOf<String>()
    private val weaponAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, weapon) }
    private val bulletAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, bullet) }
    private val basetAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, base) }
    private var adapter: DBTableRvAdapter? = null
    private var weaponID = ""
    private var bulletID = ""
    private var baseID = ""
    private val observergetWeaponList = Observer<MutableList<String>> {
        weapon.clear()
        weapon.addAll(it)

        weaponAdapter.clear()
        weaponAdapter.addAll(it)
        weaponAdapter.notifyDataSetChanged()

        dbtable_weapon?.setAdapter(weaponAdapter)

    }
    private val observergetBulletList = Observer<MutableList<String>> {
        bullet.clear()
        bullet.addAll(it)

        bulletAdapter.clear()
        bulletAdapter.addAll(it)
        bulletAdapter.notifyDataSetChanged()

        dbtable_weapon_bullet?.setAdapter(bulletAdapter)

    }
    private val observergetBaseList = Observer<MutableList<String>> {
        base.clear()
        base.addAll(it)

        basetAdapter.clear()
        basetAdapter.addAll(it)
        basetAdapter.notifyDataSetChanged()

        dbtable_weapon_base?.setAdapter(basetAdapter)

    }
    private val observergetWeaponTable = Observer<MutableList<Table>> {
        updateList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbtable)

        dbtable_weapon?.setOnItemClickListener { parent, view, position, id ->
            run {
                weaponID = weaponAdapter.getItem(position).toString()
                presenter.getWeaponTableFromDB(weaponID)
                presenter.getWeaponBase(weaponID)
            }
        }
        button_dbtable_reset.setOnClickListener {
            presenter.initWeapon(weaponID)
        }
        dbtable_weapon_bullet?.setOnItemClickListener { parent, view, position, id ->
            run {
                bulletID = bulletAdapter.getItem(position).toString()
                presenter.updateTableWeapoon(weaponID = weaponID, baseID = baseID, bulletID = bulletID)
            }
        }
        dbtable_weapon_base?.setOnItemClickListener { parent, view, position, id ->
            run {
                baseID = basetAdapter.getItem(position).toString()
                presenter.getWeaponBullet(weaponID, baseID)
            }
        }
        button_dbtable_save.setOnClickListener {
            presenter.saveWeaponTableIntoDB(weaponID)

            this.finish()
        }
        button_dbtable_add.setOnClickListener {
            presenter.currentTable.add(Table())

            updateList()
        }
    }

    override fun onStart() {
        super.onStart()
        subscribe()
        presenter.getWeaponFromDB()

        updateList()
    }

    override fun onStop() {
        super.onStop()

        unsubscribe()
    }
    private fun subscribe(){
        presenter.subscribegetWeaponList().observeForever(observergetWeaponList)
        presenter.subscribegetWeaponTable().observeForever(observergetWeaponTable)
        presenter.subscribegetBullet().observeForever(observergetBulletList)
        presenter.subscribegetBase().observeForever(observergetBaseList)
    }
    private fun unsubscribe(){
        presenter.subscribegetWeaponList().removeObserver(observergetWeaponList)
        presenter.subscribegetWeaponTable().removeObserver(observergetWeaponTable)
        presenter.subscribegetBullet().removeObserver(observergetBulletList)
        presenter.subscribegetBase().removeObserver(observergetBaseList)
    }

    private fun updateList() {
        adapter ?: initRecyclerView()
        adapter?.let {
            it.updateDataUser()
        }
    }

    private fun initRecyclerView() {

        adapter = DBTableRvAdapter()

        dbtable_rv.layoutManager = LinearLayoutManager(this)
        dbtable_rv.adapter = adapter
        dbtable_rv.setHasFixedSize(true)
    }
}