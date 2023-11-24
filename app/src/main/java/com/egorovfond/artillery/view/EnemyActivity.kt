package com.egorovfond.artillery.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.TargetResultRvAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_enemy.*
import kotlinx.android.synthetic.main.activity_orudie_settings.weapon_settings_h

class EnemyActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private val adapter by lazy { TargetResultRvAdapter() }

    private val observerUpdate = Observer<Boolean> {
        updateList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_enemy)

        btn_map_target.setOnClickListener {
            presenter.map_settings = 3

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)
        }

        enemy_target_rv.layoutManager = LinearLayoutManager(this)
        enemy_target_rv.adapter = adapter
        enemy_target_rv.setHasFixedSize(true)

        ed_enemy_name.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                presenter.getTargetList()[presenter.currentEnemy.position].nameTarget = p0.toString()
            }

        })

        checkBox_enemy.setOnClickListener {
            presenter.getTargetList()[presenter.currentEnemy.position].k_use = checkBox_enemy.isChecked

            updateList()
        }

        ed_x_target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].x = 0f
                else presenter.getTargetList()[presenter.currentEnemy.position].x = p0.toString().toFloat()
            }
        })
        ed_y_target.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].y = 0f
                else presenter.getTargetList()[presenter.currentEnemy.position].y = p0.toString().toFloat()
            }
        })
        ed_ht.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].h = 0
                else  presenter.getTargetList()[presenter.currentEnemy.position].h = p0.toString().toInt()
            }
        })
        kor_left_metr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_left = 0
                else  presenter.getTargetList()[presenter.currentEnemy.position].k_left = p0.toString().toInt()
            }
        })
        kor_right_metr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_right = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_right = p0.toString().toInt()
            }
        })
        kor_closer_metr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_down = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_down = p0.toString().toInt()
            }
        })
        kor_farther_metr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_up = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_up = p0.toString().toInt()
            }
        })

        btn_map_target_prilet.setOnClickListener {
            presenter.map_settings = 4

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)
        }
        btn_map_ed_height.setOnClickListener{
            Toast.makeText(this@EnemyActivity, "Вычисляю высоту..", Toast.LENGTH_LONG).show()
            try {
                val image = ImageView(this)
                val path = "file:///android_asset/${presenter.url}.png"
                Picasso.get().load(path)
                    .resize((presenter.heightMap.mapWigth)/10, presenter.heightMap.mapHeight/10)
                    .onlyScaleDown()
                    //.fit()
                    .into(image, object : Callback {
                        override fun onSuccess() {
                            val bitmap = (image.drawable as BitmapDrawable).bitmap
                            bitmap?.let {
                                presenter.getTargetList()[presenter.currentEnemy.position].h = presenter.getHeight(
                                    it,
                                    presenter.getTargetList()[presenter.currentEnemy.position].x,
                                    presenter.getTargetList()[presenter.currentEnemy.position].y
                                ).toInt()

                                Toast.makeText(this@EnemyActivity, "Высота цели: ${presenter.getTargetList()[presenter.currentEnemy.position].h}", Toast.LENGTH_LONG).show()
                                ed_ht.setText(presenter.getTargetList()[presenter.currentEnemy.position].h.toString())
                            }
                        }

                        override fun onError(e: java.lang.Exception?) {
                            Toast.makeText(this@EnemyActivity, "Не удалось загрузить карту высот: ${e!!.message}", Toast.LENGTH_LONG).show()
                        }

                    })
            }catch (e: Exception){
                Toast.makeText(this, "Не удалось загрузить карту высот: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onStart() {
        super.onStart()

        subscribe()
        updateList()
    }
    override fun onStop() {
        super.onStop()

        unsubscribe()
    }

    private fun subscribe(){
        presenter.subscribeUpdateResult().observeForever(observerUpdate)
    }
    private fun unsubscribe(){
        presenter.subscribeUpdateResult().removeObserver(observerUpdate)
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun updateList() {
        ed_enemy_name.setText(presenter.getTargetList()[presenter.currentEnemy.position].nameTarget)
        ed_x_target.setText(presenter.getTargetList()[presenter.currentEnemy.position].x.toString())
        ed_y_target.setText(presenter.getTargetList()[presenter.currentEnemy.position].y.toString())
        ed_ht.setText(presenter.getTargetList()[presenter.currentEnemy.position].h.toString())
        kor_left_metr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_left.toString())
        kor_right_metr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_right.toString())
        kor_closer_metr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_down.toString())
        kor_farther_metr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_up.toString())

        checkBox_enemy.isChecked = presenter.getTargetList()[presenter.currentEnemy.position].k_use

        if(checkBox_enemy.isChecked){
            linearLayout_enemy_kor_leftreight.visibility = View.VISIBLE
            linearLayout_enemy_kor_updown.visibility = View.VISIBLE
        }else{
            linearLayout_enemy_kor_leftreight.visibility = View.GONE
            linearLayout_enemy_kor_updown.visibility = View.GONE
        }
        adapter.notifyDataSetChanged()
    }
}