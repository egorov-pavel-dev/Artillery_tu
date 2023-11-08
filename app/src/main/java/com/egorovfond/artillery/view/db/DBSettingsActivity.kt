package com.egorovfond.artillery.view.db

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.egorovfond.artillery.R
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.MapsActivity
import kotlinx.android.synthetic.main.activity_dbsettings.*

class DBSettingsActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dbsettings)

        button_settingsdb_weapon.setOnClickListener {
            val intent = Intent(this, DBWeaponActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_bullet.setOnClickListener {
            val intent = Intent(this, DBBulletActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_table.setOnClickListener {
            val intent = Intent(this, DBTableActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_maps.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }
        button_settingsdb_reset.setOnClickListener {
            presenter.reset()
        }
        localMap.setOnClickListener {
            presenter.localmap = localMap.isChecked
        }

        localMap.isChecked = presenter.localmap
    }
}