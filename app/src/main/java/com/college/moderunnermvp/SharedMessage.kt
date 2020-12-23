package com.college.moderunnermvp

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*
import kotlin.concurrent.fixedRateTimer

class SharedMessage : ViewModel() {
    var SpeedometerFragmentMsg = ""
    var SettingsFragmentMsg = ""
    var SpeedometerFragmentModel: Vector<GpsSample> = Vector(0,1)
    var HistoryFragmentModel: Vector<DataSample> = Vector(0,1)

    override fun onCleared() {
        super.onCleared()
        Log.d("Trace", "View Model Cleared")
    }

    inner class UIModel{
//to share data around the app
        fun averageSpeed(): Double
        {
            return SpeedometerFragmentModel.lastElement().totalDistance / SpeedometerFragmentModel.lastElement().overallTime

        }
        fun instantaneousAverageSpeed(): Double {
            var sampleCount = 0
            var sampleDistanceTotal = 0.0
            var overallTime = 0.0
            //if current speed is 0 we dont add anymore to count
            for(sample in SpeedometerFragmentModel) {
                if (sample.speedFrame > 1.34) {
                    sampleCount++
                    sampleDistanceTotal += sample.distanceFrame
                    overallTime = sample.overallTime
                }
            }
            return sampleDistanceTotal / overallTime
        }
    }
}