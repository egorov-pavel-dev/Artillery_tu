package com.egorovfond.artillery.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import kotlinx.android.synthetic.main.activity_popravki.*

class PopravkiActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_popravki)

        button_save_popravki.setOnClickListener {
            var windspeed = 0f
            if(wind_speed.text!!.isNotEmpty()) windspeed = wind_speed.text.toString().toFloat()
            var windcross = 0
            if(wind_ugol.text!!.isNotEmpty()) windcross = wind_ugol.text.toString().toInt()
            var hum= 0f
            if(humidity.text!!.isNotEmpty()) hum = humidity.text.toString().toFloat()
            var teperature = 0f
            if(temp.text!!.isNotEmpty()) teperature = temp.text.toString().toFloat()
            var radius_ = 0
            if(radius.text!!.isNotEmpty()) radius_ = radius.text.toString().toInt()

            presenter.onClick_Save_Popravki(
                windspeed,
                windcross,
                hum,
                teperature,
                radius_
            )

            this.finish()
        }

        wind_speed.setText(presenter.getPopravki().wind_speed.toString())
        wind_ugol.setText(presenter.getPopravki().wind_cross.toString())
        humidity.setText(presenter.getPopravki().humidity.toString())
        temp.setText(presenter.getPopravki().temp.toString())
        radius.setText(presenter.getPopravki().radius.toString())
    }
}