package com.egorovfond.artillery.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egorovfond.artillery.databinding.ActivityPopravkiBinding
import com.egorovfond.artillery.presenter.Presenter

class PopravkiActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private lateinit var binding: ActivityPopravkiBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPopravkiBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //setContentView(R.layout.activity_popravki)

        binding.buttonSavePopravki.setOnClickListener {
            var windspeed = 0f
            if(binding.windSpeed.text!!.isNotEmpty()) windspeed = binding.windSpeed.text.toString().toFloat()
            var windcross = 0
            if(binding.windUgol.text!!.isNotEmpty()) windcross = binding.windUgol.text.toString().toInt()
            var hum= 0f
            if(binding.humidity.text!!.isNotEmpty()) hum = binding.humidity.text.toString().toFloat()
            var teperature = 0f
            if(binding.temp.text!!.isNotEmpty()) teperature = binding.temp.text.toString().toFloat()
            var radius_ = 0
            if(binding.radius.text!!.isNotEmpty()) radius_ = binding.radius.text.toString().toInt()

            presenter.onClick_Save_Popravki(
                windspeed,
                windcross,
                hum,
                teperature,
                radius_
            )

            this.finish()
        }

        binding.windSpeed.setText(presenter.getPopravki().wind_speed.toString())
        binding.windUgol.setText(presenter.getPopravki().wind_cross.toString())
        binding.humidity.setText(presenter.getPopravki().humidity.toString())
        binding.temp.setText(presenter.getPopravki().temp.toString())
        binding.radius.setText(presenter.getPopravki().radius.toString())
    }
}