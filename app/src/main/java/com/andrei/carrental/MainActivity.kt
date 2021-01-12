package com.andrei.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.andrei.UI.fragments.CurrentLocationFragment
import com.andrei.UI.fragments.HomeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        supportFragmentManager.beginTransaction().replace(R.id.container_main,HomeFragment()).commit()
    }
}