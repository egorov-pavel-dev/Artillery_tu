package com.egorovfond.artillery.view

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.databinding.ActivityEnemyBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.TargetResultRvAdapter
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso

class EnemyActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private val adapter by lazy { TargetResultRvAdapter() }
    private lateinit var binding: ActivityEnemyBinding


    private val observerUpdate = Observer<Boolean> {
        adapter.notifyDataSetChanged()
        //updateList()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_enemy)
        binding = ActivityEnemyBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.btnMapTarget.setOnClickListener {
            presenter.map_settings = 3

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)
        }

        binding.enemyTargetRv.layoutManager = LinearLayoutManager(this)
        binding.enemyTargetRv.adapter = adapter
        binding.enemyTargetRv.setHasFixedSize(true)

        binding.edEnemyName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                presenter.getTargetList()[presenter.currentEnemy.position].nameTarget = p0.toString()
            }

        })

        binding.checkBoxEnemy.setOnClickListener {
            presenter.getTargetList()[presenter.currentEnemy.position].k_use = binding.checkBoxEnemy.isChecked

            updateList()
        }

        binding.edXTarget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].x = 0f
                else presenter.getTargetList()[presenter.currentEnemy.position].x = p0.toString().toFloat()
            }
        })
        binding.edYTarget.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].y = 0f
                else presenter.getTargetList()[presenter.currentEnemy.position].y = p0.toString().toFloat()
            }
        })
        binding.edHt.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].h = 0
                else  presenter.getTargetList()[presenter.currentEnemy.position].h = p0.toString().toInt()
            }
        })
        binding.korLeftMetr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_left = 0
                else  presenter.getTargetList()[presenter.currentEnemy.position].k_left = p0.toString().toInt()
            }
        })
        binding.korRightMetr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_right = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_right = p0.toString().toInt()
            }
        })
        binding.korCloserMetr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_down = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_down = p0.toString().toInt()
            }
        })
        binding.korFartherMetr.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
            override fun afterTextChanged(p0: Editable?) {
                if(p0.toString().isEmpty()) presenter.getTargetList()[presenter.currentEnemy.position].k_up = 0
                else presenter.getTargetList()[presenter.currentEnemy.position].k_up = p0.toString().toInt()
            }
        })

        binding.btnMapTargetPrilet.setOnClickListener {
            presenter.map_settings = 4

            val intent = Intent(this, MapFragment::class.java)
            startActivity(intent)
        }
        binding.btnMapEdHeight.setOnClickListener{
            Toast.makeText(this@EnemyActivity, "Вычисляю высоту..", Toast.LENGTH_LONG).show()
            updateHeight()
        }


    }

    private fun updateHeight() {
        try {
            val image = ImageView(this)
            val part =
                (Math.round((presenter.getTargetList()[presenter.currentEnemy.position].x * 1000)) / presenter.heightMap.part).toInt()
            val path = "file:///android_asset/${presenter.url}_${part}.png"
            Picasso.get().load(path)
                //.resize((presenter.heightMap.mapWigth * presenter.heightMap.scale).toInt(), (presenter.heightMap.mapHeight * presenter.heightMap.scale).toInt())
                //.onlyScaleDown()
                .into(image, object : Callback {
                    override fun onSuccess() {
                        val bitmap = (image.drawable as BitmapDrawable).bitmap
                        bitmap?.let {
                            presenter.getTargetList()[presenter.currentEnemy.position].h =
                                presenter.getHeight(
                                    it,
                                    presenter.getTargetList()[presenter.currentEnemy.position].x,
                                    presenter.getTargetList()[presenter.currentEnemy.position].y
                                ).toInt()

                            Toast.makeText(
                                this@EnemyActivity,
                                "Высота цели: ${presenter.getTargetList()[presenter.currentEnemy.position].h}",
                                Toast.LENGTH_LONG
                            ).show()
                            binding.edHt.setText(presenter.getTargetList()[presenter.currentEnemy.position].h.toString())
                        }
                    }

                    override fun onError(e: java.lang.Exception?) {
                        Toast.makeText(
                            this@EnemyActivity,
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
        binding.edEnemyName.setText(presenter.getTargetList()[presenter.currentEnemy.position].nameTarget)
        binding.edXTarget.setText(presenter.getTargetList()[presenter.currentEnemy.position].x.toString())
        binding.edYTarget.setText(presenter.getTargetList()[presenter.currentEnemy.position].y.toString())
        binding.edHt.setText(presenter.getTargetList()[presenter.currentEnemy.position].h.toString())
        binding.korLeftMetr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_left.toString())
        binding.korRightMetr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_right.toString())
        binding.korCloserMetr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_down.toString())
        binding.korFartherMetr.setText(presenter.getTargetList()[presenter.currentEnemy.position].k_up.toString())

        binding.checkBoxEnemy.isChecked = presenter.getTargetList()[presenter.currentEnemy.position].k_use

        if (presenter.autoupdate){
            updateHeight()
        }

        if(binding.checkBoxEnemy.isChecked){
            binding.linearLayoutEnemyKorLeftreight.visibility = View.VISIBLE
            binding.linearLayoutEnemyKorUpdown.visibility = View.VISIBLE
        }else{
            binding.linearLayoutEnemyKorLeftreight.visibility = View.GONE
            binding.linearLayoutEnemyKorUpdown.visibility = View.GONE
        }

        adapter.notifyDataSetChanged()
    }
}