package com.college.moderunnermvp

import android.app.IntentService
import android.content.Intent
import android.content.Context
import android.nfc.Tag
import android.os.IBinder
import android.util.Log
import androidx.core.app.JobIntentService


class SpeedometerService : IntentService("Speedometer Service") {


    var Tag: String = "IntentService"

    override fun onHandleIntent(intent: Intent?) {
        Log.i(Tag, "onHandleWork")
        Thread.sleep(5000)
        Log.i("Tag","done")
        var distanceInt = intent?.getIntExtra("DistanceToRun", 0)
        var i = distanceInt?.times(2).toString()
        Log.i("Tag",i)
    }




}





