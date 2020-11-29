package com.college.moderunnermvp

import android.Manifest
import android.app.IntentService
import android.content.Intent



class SpeedometerService : IntentService("Speedometer Service")
{


    var Tag: String = "IntentService"


    override fun onHandleIntent(intent: Intent?) {







        /*Log.i(Tag, "onHandleWork")
        Thread.sleep(5000)
        Log.i("Tag","done")
        var distanceInt = intent?.getIntExtra("DistanceToRun", 0)
        var i = distanceInt?.times(2).toString()
        Log.i("Tag",i)*/

    }




}





