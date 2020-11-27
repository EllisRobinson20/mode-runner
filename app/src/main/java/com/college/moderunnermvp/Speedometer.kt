package com.college.moderunnermvp

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.IBinder
import android.view.View
import kotlinx.android.synthetic.main.activity_speedometer.*

class Speedometer : AppCompatActivity() {

    var mService: SpeedometerService? = null
    var isBound: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        val intent = Intent(this, SpeedometerService::class.java)
        //bindService(intent, myConnection, Context.BIND_AUTO_CREATE)
    }

    fun startSpeedometerService(view: View) {
        /*val text_received = distance_input.text.toString().toInt()
        val Count = mService?.getResult(text_received)
        distance_output.text = "$Count"*/
    }

    /*private val myConnection = object : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            val binder = service as SpeedometerService.MyBinder
            mService = binder.getService()
            isBound = true
        }

    }*/


   /* fun startSpeedometerService(view: View) {
        var intent = Intent(this, SpeedometerService::class.java)
        startService(intent)
    }*/
}