package com.egorovfond.artillery.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.egorovfond.artillery.databinding.ActivityOrudieBinding
import com.egorovfond.artillery.presenter.Presenter
import com.egorovfond.artillery.view.rvAdapter.OrudieRvAdapter

class OrudieActivity : AppCompatActivity() {
    private val presenter by lazy { Presenter.getPresenter() }
    private var adapter: OrudieRvAdapter? = null
    private lateinit var binding: ActivityOrudieBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //setContentView(R.layout.activity_orudie)
        binding = ActivityOrudieBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        binding.addOrudie.setOnClickListener {
            presenter.addWeapon()
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

        adapter = OrudieRvAdapter()

        binding.orudieRv.layoutManager = LinearLayoutManager(this)
        binding.orudieRv.adapter = adapter
        binding.orudieRv.setHasFixedSize(true)
    }
}