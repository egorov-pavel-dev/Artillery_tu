package com.egorovfond.artillery.view

import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.egorovfond.artillery.R
import com.egorovfond.artillery.databinding.ActivityOrudieSettingsBinding
import com.egorovfond.artillery.math.Table
import com.egorovfond.artillery.presenter.Presenter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso


class OrudieSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityOrudieSettingsBinding

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

        binding.weaponSettingsWeapon.setAdapter(weaponAdapter)

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

        binding.orudieWeaponBase?.setAdapter(baseAdapter)

        //updateForm()
    }
    private val observergetBulletList = Observer<MutableList<String>> {
        bullet.clear()
        bullet.addAll(it)

        bulletAdapter.clear()
        bulletAdapter.addAll(it)
        bulletAdapter.notifyDataSetChanged()

        binding.orudieWeaponBullet.setAdapter(bulletAdapter)

        //updateForm()
    }
    private val observergetWeaponTable = Observer<MutableList<Table>> {
        presenter.getWeaponBase(presenter.getCurrentWeapon().weapon)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrudieSettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        //setContentView(R.layout.activity_orudie_settings)

        binding.btnMapOrudie.setOnClickListener {
            presenter.map_settings = 1

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)

        }
        binding.btnMapTh.setOnClickListener {
            presenter.map_settings = 2

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)

        }
        binding.weaponSettingsSave.setOnClickListener {
            presenter.getCurrentWeapon().weapon_save_x = presenter.getCurrentWeapon().x
            presenter.getCurrentWeapon().weapon_save_y = presenter.getCurrentWeapon().y
            presenter.getCurrentWeapon().weapon_save_azimut = presenter.getCurrentWeapon().azimut_Dot

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show()
        }
        binding.weaponSettingsLoad.setOnClickListener {
            presenter.getCurrentWeapon().x = presenter.getCurrentWeapon().weapon_save_x
            presenter.getCurrentWeapon().y = presenter.getCurrentWeapon().weapon_save_y
            presenter.getCurrentWeapon().azimut_Dot = presenter.getCurrentWeapon().weapon_save_azimut

            binding.edXMinomet.setText(presenter.getCurrentWeapon().x.toString())
            binding.edYMinomet.setText(presenter.getCurrentWeapon().y.toString())
            binding.weaponSettingsAzimutTh.setText(presenter.getCurrentWeapon().azimut_Dot.toString())

        }
        //weapon_settings_save.visibility = View.GONE

        binding.weaponSettingsUpdateTH.setOnClickListener {
            updateAzimutTh()
        }
        binding.weaponSettingsWeapon.setOnItemClickListener { parent, view, position, id ->
            run {
                positionWeapon = position
                presenter.setWeaponIntoOrudie(weaponAdapter.getItem(position).toString())
                try {
                    val table = getJSONTable(weaponAdapter.getItem(position).toString())
                    presenter.getWeaponTableFromDB(weaponAdapter.getItem(position).toString(), table)
                }catch (e: Exception) {
                    presenter.getWeaponTableFromDB(weaponAdapter.getItem(position).toString(), mutableListOf())
                }
                presenter.setBulletIntoOrudie("")
                presenter.setBaseIntoOrudie("")
            }
        }

        binding.weaponSettingsName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].nameOrudie = ""
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].nameOrudie = p0.toString()
            }
        })

        binding.edXMinomet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(binding.edXMinomet.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].x = 0f
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].x = binding.edXMinomet.text.toString().toFloat()

            }
        })
        binding.edYMinomet.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(binding.edYMinomet.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].y = 0f
                    else  presenter.getWeapon()[presenter.getCurrentWeapon().position].y = binding.edYMinomet.text.toString().toFloat()
            }
        })

        binding.edXTH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(binding.edXTH.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].x_Dot = 0f
                    else  presenter.getWeapon()[presenter.getCurrentWeapon().position].x_Dot = binding.edXTH.text.toString().toFloat()
            }
        })
        binding.edYTH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(binding.edYTH.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].y_Dot = 0f
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].y_Dot = binding.edYTH.text.toString().toFloat()
            }
        })

        binding.weaponSettingsH.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                if(binding.weaponSettingsH.text.toString().isEmpty()) presenter.getWeapon()[presenter.getCurrentWeapon().position].h = 0
                else  presenter.getWeapon()[presenter.getCurrentWeapon().position].h = binding.weaponSettingsH.text.toString().toInt()
            }
        })
        binding.weaponSettingsAzimutTh.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }
            override fun afterTextChanged(p0: Editable?) {
                    if(binding.weaponSettingsAzimutTh.text.toString().isEmpty())
                        presenter.setAzimutDot(presenter.getWeapon()[presenter.getCurrentWeapon().position], 0)
                    else
                        presenter.setAzimutDot(presenter.getWeapon()[presenter.getCurrentWeapon().position], binding.weaponSettingsAzimutTh.text.toString().toInt())
            }
        })

        binding.orudieWeaponBullet?.setOnItemClickListener { parent, view, position, id ->
            run {
                presenter.setBulletIntoOrudie(bulletAdapter.getItem(position).toString())
            }
        }
        binding.orudieWeaponBase?.setOnItemClickListener { parent, view, position, id ->
            run {
                presenter.setBaseIntoOrudie(baseAdapter.getItem(position).toString())
                presenter.setBulletIntoOrudie("")
            }
        }

        binding.btnMapHeight.setOnClickListener {
            Toast.makeText(this@OrudieSettingsActivity, "Вычисляю высоту..", Toast.LENGTH_LONG).show()
            updateHeight()
        }
    }

    private fun updateHeight() {
        try {
            val image = ImageView(this)
            val part =
                (Math.round((presenter.getCurrentWeapon().x * 1000)) / presenter.heightMap.part).toInt()
            val path = "file:///android_asset/${presenter.url}_${part}.png"
            Picasso.get().load(path)
                .into(image, object : Callback {
                    override fun onSuccess() {
                        val bitmap = (image.drawable as BitmapDrawable).bitmap
                        bitmap?.let {
                            presenter.getCurrentWeapon().h = presenter.getHeight(
                                it,
                                presenter.getCurrentWeapon().x,
                                presenter.getCurrentWeapon().y
                            ).toInt()

                            Toast.makeText(
                                this@OrudieSettingsActivity,
                                "Высота орудия: ${presenter.getCurrentWeapon().h}",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.weaponSettingsH.setText(presenter.getCurrentWeapon().h.toString())
                        }
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Toast.makeText(
                            this@OrudieSettingsActivity,
                            "Не удалось загрузить карту высот: ${e!!.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }

                })

        } catch (e: Exception) {
            Toast.makeText(
                this,
                "Не удалось загрузить карту высот: ${e.message}",
                Toast.LENGTH_LONG
            ).show()
        }
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
        binding.edXMinomet.setText(presenter.getCurrentWeapon().x.toString())
        binding.edYMinomet.setText(presenter.getCurrentWeapon().y.toString())
        binding.edXTH.setText(presenter.getCurrentWeapon().x_Dot.toString())
        binding.edYTH.setText(presenter.getCurrentWeapon().y_Dot.toString())
        binding.weaponSettingsH.setText(presenter.getCurrentWeapon().h.toString())
        binding.weaponSettingsAzimutTh.setText(presenter.getCurrentWeapon().azimut_Dot.toString())
        binding.weaponSettingsName.setText(presenter.getCurrentWeapon().nameOrudie)
        binding.weaponSettingsWeapon.hint = presenter.getCurrentWeapon().weapon
        binding.orudieWeaponBullet.hint = if (presenter.getCurrentWeapon().bullet.isEmpty()) {"Автовыбор"} else presenter.getCurrentWeapon().bullet
        binding.orudieWeaponBase.hint = if (presenter.getCurrentWeapon().base.isEmpty()) {"не задано"} else presenter.getCurrentWeapon().base
        if (presenter.autoupdate){
            updateHeight()
            updateAzimutTh()
        }
    }

    private fun updateAzimutTh() {
        presenter.updateOrudieByXY(presenter.getWeapon()[presenter.getCurrentWeapon().position])
        binding.weaponSettingsAzimutTh.setText(presenter.getCurrentWeapon().azimut_Dot.toString())
    }

    private fun getJSONTable(name: String): MutableList<Table> {
        val gson = Gson()
        val inputString = this.baseContext.assets.open("${name}.json")
                .bufferedReader()
                .use { it.readText() }
        val listType = object : TypeToken<MutableList<Table?>?>() {}.type

        val table: MutableList<Table> = gson.fromJson(inputString, listType) as MutableList<Table>

        return table
    }
}