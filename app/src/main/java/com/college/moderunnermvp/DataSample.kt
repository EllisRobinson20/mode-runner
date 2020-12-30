package com.college.moderunnermvp

import android.icu.text.TimeZoneFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

@RequiresApi(Build.VERSION_CODES.O)
class DataSample(val date:String, val targetDistance:String, val finishTime:String, val topSpeed:String, val averageSpeed:String, val peakAcceleration:String, val finalDistance:String) {

    //var parsedDateTime = initTimeStamp()

    var targetDistanceNumber = 0.0
    var finishTimeNumber = 0.0
    var topSpeedNumber = 0.0
    var averageSpeedNumber = 0.0
    var peakAccelerationNumber = 0.0
    var finalDistanceNumber = 0.0

    var stringList = listOf<String>()

    init {
        stringList = finishTime.split(".").map { it -> it.trim()}
        Log.i("init datasample", stringList.toString())
        targetDistanceNumber = targetDistance.toDouble()
        finishTimeNumber = finishTime.toDouble()
        finishTime
        if (topSpeed != "No Record")
            topSpeedNumber = topSpeed.toDouble()
        if (averageSpeed != "No Record")
            averageSpeedNumber = averageSpeed.toDouble()
        if (peakAcceleration != "No Record")
            peakAccelerationNumber = peakAcceleration.toDouble()
        finalDistanceNumber = finalDistance.toDouble()
    }
    fun initTimeStamp(): LocalDateTime {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) //could throw error! did so during init so commented out this.parsedDateTime
    }
    fun initTime(): String {
         return when (stringList.size) {
            2 -> return LocalTime.of(0, 0, stringList[0].toInt() ,stringList[1].toInt()).toString()
            3 -> return LocalTime.of(0, stringList[0].toInt(), stringList[1].toInt() ,stringList[2].toInt()).toString()
            4 -> return LocalTime.of(stringList[0].toInt(), stringList[1].toInt(), stringList[2].toInt() ,stringList[3].toInt()).toString()
             else -> LocalTime.of(0, 0,0,0).toString()
         }

    }

}