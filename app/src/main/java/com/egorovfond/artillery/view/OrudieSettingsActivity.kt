package com.egorovfond.artillery.view

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.egorovfond.artillery.R
import com.egorovfond.artillery.math.Table
import com.egorovfond.artillery.presenter.Presenter
import kotlinx.android.synthetic.main.activity_orudie_settings.*

class OrudieSettingsActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private val weapon = mutableListOf<String>()
    private val bullet = mutableListOf<String>()
    private val base = mutableListOf<String>()
    private var positionWeapon = 0

    private val baseAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, base) }
    private val bulletAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, bullet) }
    private val weaponAdapter by lazy{ ArrayAdapter(this, R.layout.list_item, weapon) }
    private val observergetWeaponList = Observer<MutableList<String>> {
        weapon.clear()
        weapon.addAll(it)

        weaponAdapter.clear()
        weaponAdapter.addAll(it)
        weaponAdapter.notifyDataSetChanged()

        weapon_settings_weapon?.setAdapter(weaponAdapter)

        //updateForm()
    }
    private val observergetBaseList = Observer<MutableList<String>> {
        base.clear()
        base.addAll(it)
        if (it.size == 0){
            presenter.setBaseIntoOrudie("")
            presenter.getWeaponBullet(presenter.getCurrentWeapon().weapon, "")
        }
        else {
            if (presenter.getCurrentWeapon().base.isEmpty()) presenter.setBaseIntoOrudie(it[0])

            presenter.getWeaponBullet(presenter.getCurrentWeapon().weapon, presenter.getCurrentWeapon().base)
        }

        baseAdapter.clear()
        baseAdapter.addAll(it)
        baseAdapter.notifyDataSetChanged()

        orudie_weapon_base?.setAdapter(baseAdapter)

        //updateForm()
    }
    private val observergetBulletList = Observer<MutableList<String>> {
        bullet.clear()
        bullet.addAll(it)

        bulletAdapter.clear()
        bulletAdapter.addAll(it)
        bulletAdapter.notifyDataSetChanged()

        orudie_weapon_bullet?.setAdapter(bulletAdapter)

        //updateForm()
    }
    private val observergetWeaponTable = Observer<MutableList<Table>> {
        presenter.getWeaponBase(presenter.getCurrentWeapon().weapon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orudie_settings)

        btn_map_orudie.setOnClickListener {
            presenter.map_settings = 1

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)

        }
        btn_map_th.setOnClickListener {
            presenter.map_settings = 2

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)

        }
        weapon_settings_save.setOnClickListener {
            presenter.getCurrentWeapon().weapon_save_x = presenter.getCurrentWeapon().x
            presenter.getCurrentWeapon().weapon_save_y = presenter.getCurrentWeapon().y
            presenter.getCurrentWeapon().weapon_save_azimut = presenter.getCurrentWeapon().azimut_Dot

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
        }
        weapon_settings_load.setOnClickListener {
            presenter.getCurrentWeapon().x = presenter.getCurrentWeapon().weapon_save_x
            presenter.getCurrentWeapon().y = presenter.getCurrentWeapon().weapon_save_y
            presenter.getCurrentWeapon().azimut_Dot = presenter.getCurrentWeapon().weapon_save_azimut

            ed_x_minomet.setText(presenter.getCurrentWeapon().x.toString())
            ed_y_minomet.setText(presenter.getCurrentWeapon().y.toString())
            weapon_settings_azimut_th.setText(presenter.getCurrentWeapon().azimut_Dot.toString())

        }
        //weapon_settings_save.visibility = View.GONE

        weapon_settings_updateTH.setOnClickListener {
            updateAzimutTh()
        }
        weapon_settings_weapon?.setOnItemClickListener { parent, view, position, id ->
            run {
                positionWeapon = position
                presenter.setWeaponIntoOrudie(weaponAdapter.getItem(position).toString())
                presenter.getWeaponTableFromDB(weaponAdapter.getItem(position).toString())
                presenter.setBulletIntoOrudie("")
                presenter.setBaseIntoOrudie("")
            }
        }

        weapon_settings_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].nameOrudie = ""
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].nameOrudie = p0.toString()

            }
        })

        ed_x_minomet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(ed_x_minomet.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].x = 0f
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].x = ed_x_minomet.text.toString().toFloat()

            }
        })
        ed_y_minomet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(ed_y_minomet.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].y = 0f
                    else  presenter.getWeapon()[presenter.getCurrentWeapon().position].y = ed_y_minomet.text.toString().toFloat()
            }
        })

        ed_x_TH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(ed_x_TH.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].x_Dot = 0f
                    else  presenter.getWeapon()[presenter.getCurrentWeapon().position].x_Dot = ed_x_TH.text.toString().toFloat()
            }
        })
        ed_y_TH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(ed_y_TH.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].y_Dot = 0f
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].y_Dot = ed_y_TH.text.toString().toFloat()
            }
        })

        weapon_settings_h.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(weapon_settings_h.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].h = 0
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].h = weapon_settings_h.text.toString().toInt()
            }
        })
        weapon_settings_azimut_th.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(weapon_settings_azimut_th.text.toString().isEmpty())
                        presenter.setAzimutDot(presenter.getWeapon()[presenter.getCurrentWeapon().position], 0)
                    else
                        presenter.setAzimutDot(presenter.getWeapon()[presenter.getCurrentWeapon().position], weapon_settings_azimut_th.text.toString().toInt())
            }
        })

        orudie_weapon_bullet?.setOnItemClickListener { parent, view, position, id ->
            run {
                presenter.setBulletIntoOrudie(bulletAdapter.getItem(position).toString())
            }
        }
        orudie_weapon_base?.setOnItemClickListener { parent, view, position, id ->
            run {
                presenter.setBaseIntoOrudie(baseAdapter.getItem(position).toString())
                presenter.setBulletIntoOrudie("")
            }
        }
    }

    private fun save() {
//        if (weapon_settings_azimut_th.text?.equals("") == true || weapon_settings_azimut_th.text?.toString()?.toInt()  == 0){
//            presenter.updateOrudieByXY(presenter.getCurrentWeapon())
//        }else{
//            presenter.updateTHbyAzimut(presenter.getCurrentWeapon())
//        }
//
//        presenter.saveOrudie()
    }

    override fun onStart() {
        super.onStart()

        subscribe()
        presenter.getWeaponFromDB()

        updateForm()
    }
    override fun onStop() {
        super.onStop()

        unsubscribe()
    }

    private fun subscribe(){
        presenter.subscribegetWeaponList().observeForever(observergetWeaponList)
        presenter.subscribegetBullet().observeForever(observergetBulletList)
        presenter.subscribegetBase().observeForever(observergetBaseList)
        presenter.subscribegetWeaponTable().observeForever(observergetWeaponTable)
    }
    private fun unsubscribe(){
        presenter.subscribegetWeaponList().removeObserver(observergetWeaponList)
        presenter.subscribegetBullet().removeObserver(observergetBulletList)
        presenter.subscribegetBase().removeObserver(observergetBaseList)
        presenter.subscribegetWeaponTable().removeObserver(observergetWeaponTable)
    }

    private fun updateForm() {
        ed_x_minomet.setText(presenter.getCurrentWeapon().x.toString())
        ed_y_minomet.setText(presenter.getCurrentWeapon().y.toString())
        ed_x_TH.setText(presenter.getCurrentWeapon().x_Dot.toString())
        ed_y_TH.setText(presenter.getCurrentWeapon().y_Dot.toString())
        weapon_settings_h.setText(presenter.getCurrentWeapon().h.toString())
        weapon_settings_azimut_th.setText(presenter.getCurrentWeapon().azimut_Dot.toString())
        weapon_settings_name.setText(presenter.getCurrentWeapon().nameOrudie)
        weapon_settings_weapon.hint = presenter.getCurrentWeapon().weapon
        orudie_weapon_bullet.hint = if (presenter.getCurrentWeapon().bullet.isEmpty()) {"Автовыбор"} else presenter.getCurrentWeapon().bullet
        orudie_weapon_base.hint = if (presenter.getCurrentWeapon().base.isEmpty()) {"не задано"} else presenter.getCurrentWeapon().base
//
//        if (presenter.getCurrentWeapon().weapon.isEmpty()) positionWeapon = -1
//        else positionWeapon = weaponAdapter.getPosition(presenter.getCurrentWeapon().weapon)
//
//        if (presenter.getCurrentWeapon().base.isEmpty()) positionBase = -1
//        else positionBase = baseAdapter.getPosition(presenter.getCurrentWeapon().base)
//
//        if (presenter.getCurrentWeapon().bullet.isEmpty()) positionBullet = -1
//        else positionBullet = bulletAdapter.getPosition(presenter.getCurrentWeapon().bullet)
    }
    private fun updateAzimutTh() {
        presenter.updateOrudieByXY(presenter.getWeapon()[presenter.getCurrentWeapon().position])
        weapon_settings_azimut_th.setText(presenter.getCurrentWeapon().azimut_Dot.toString())
    }
}