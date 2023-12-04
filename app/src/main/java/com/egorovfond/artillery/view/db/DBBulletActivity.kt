package com.egorovfond.artillery.view.db

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.egorovfond.artillery.databinding.ActivityDbbulletBinding

class DBBulletActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDbbulletBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_dbbullet)
        binding = ActivityDbbulletBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

    }
}