package com.egorovfond.artillery.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.R
import com.egorovfond.artillery.database.DB
import com.egorovfond.artillery.database.room.database.AppDatabase
import com.egorovfond.artillery.databinding.ActivityMainBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.db.DBSettingsActivity
import com.egorovfond.artillery.view.rvAdapter.TargetRvAdapter
import com.google.android.play.core.splitcompat.SplitCompat

const val PREFERENCE_NOT_FIRST_START = "FIRST_START"

class MainActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var adapter: TargetRvAdapter? = null
    private val db by lazy { AppDatabase.getAppDataBase(context = this) }
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        SplitCompat.install(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_main)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val settings = getSharedPreferences(PREFERENCE_NOT_FIRST_START, Context.MODE_PRIVATE)
        val notFirstStart = settings.getBoolean(PREFERENCE_NOT_FIRST_START, false)

        DB.weaponDao = db?.weaponDao()
        DB.tableDao = db?.tableDao()

        if (!notFirstStart){
            db?.let {
                presenter.initializeDB()
                settings.edit().putBoolean(PREFERENCE_NOT_FIRST_START, true).apply()
            }
        }

        binding.buttonOrudie.setOnClickListener {
            val intent = Intent(this, OrudieActivity::class.java)
            startActivity(intent)
        }

        binding.buttonPopravki.setOnClickListener {
            val intent = Intent(this, PopravkiActivity::class.java)
            startActivity(intent)
        }
        binding.buttonAddTarget.setOnClickListener {
            presenter.addTarget()

            updateList()
        }

    }

    override fun onStart() {
        super.onStart()

        updateList()
    }

    override fun onDestroy() {
        super.onDestroy()

        db?.close()
        AppDatabase.destroyDataBase()
    }
    private fun updateList() {
        adapter ?: initRecyclerView()
        adapter?.let {
            it.updateDataUser()
        }
    }

    private fun initRecyclerView() {

        adapter = TargetRvAdapter()

        binding.targetRv.layoutManager = LinearLayoutManager(this)
        binding.targetRv.adapter = adapter
        binding.targetRv.setHasFixedSize(true)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_main_settings_db -> {
                val intent = Intent(this, DBSettingsActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}

