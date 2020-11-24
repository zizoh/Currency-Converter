package com.zizohanto.android.currencyconverter.converter.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.zizohanto.android.currencyconverter.converter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}