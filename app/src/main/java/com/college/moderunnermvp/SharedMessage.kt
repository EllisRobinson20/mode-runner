package com.college.moderunnermvp

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import java.util.*
import kotlin.concurrent.fixedRateTimer

class SharedMessage : ViewModel() {
    var SpeedometerFragmentMsg = ""
    var SettingsFragmentMsg = ""
    var SpeedometerFragmentModel: Vector<GpsSample> = Vector(0,1)
    var HistoryFragmentModel: Vector<DataSample> = Vector(0,1)
    //// Test data
    var distanceFrame: Float = 10.0f
    var timeFrame: Double = 5.0
    var overallTime: Double = 5.0
    var topSpeed: Double = 1.0
    var target: Int = 100
    var totalDistance: Double = 10.0
    var serviceIsRunning: Boolean = true
    var serviceComplete: Boolean = false
    var latLong: LatLng = LatLng(0.0,0.0)
    //// Test method
    fun mockData() {
        var Distancetotal = 0.0
        var timeTotal = 1.0
        for (i in 1..20) {
            var gpsSample: GpsSample = GpsSample(2.0f,1.0, timeTotal,topSpeed,target,Distancetotal,serviceIsRunning, serviceComplete,latLong)
            Distancetotal += gpsSample.distanceFrame.toDouble()
            timeTotal += gpsSample.timeFrame
            SpeedometerFragmentModel.addElement(gpsSample)
        }
    }
    init {
        //mockData()
    }


    inner class UIModel{

//to share data around the app
        fun averageSpeed(): Double
        {
            return SpeedometerFragmentModel.lastElement().totalDistance / SpeedometerFragmentModel.lastElement().overallTime

        }
        fun instantaneousAverageSpeed(): Double { // required work around the lower than expected average result with averageSpeed
            var sampleCount = 0
            var sampleDistanceTotal = 0.0
            var overallTime = 0.0
            //if current speed is 0 we dont add anymore to count
            for(sample in SpeedometerFragmentModel) {
                if (sample.speedFrame > 1.34) { // dont count values that are too slow as they reduce the average
                    sampleCount++
                    sampleDistanceTotal += sample.distanceFrame
                    overallTime = sample.overallTime
                }
            }
            return sampleDistanceTotal / overallTime
        }
    }
}