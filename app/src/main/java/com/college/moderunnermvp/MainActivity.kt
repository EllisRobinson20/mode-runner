package com.college.moderunnermvp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity(), TimeTracker.OnFragmentInteraction {
    val settingFragment = FragmentSettings()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onFragmentInteraction(someText: String) {

    }
}