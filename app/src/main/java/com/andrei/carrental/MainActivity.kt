package com.andrei.carrental

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.andrei.UI.fragments.CurrentLocationFragment
import com.andrei.UI.fragments.ExpandedCarFragment
import com.andrei.UI.fragments.HomeFragment
import com.andrei.carrental.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        supportFragmentManager.beginTransaction().replace(R.id.container_main,ExpandedCarFragment()).commit()
    }
}