package com.college.moderunnermvp

import android.os.Build
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
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

    init {
        targetDistanceNumber = targetDistance.toDouble()
        finishTimeNumber = finishTime.toDouble()
        topSpeedNumber = topSpeed.toDouble()
        averageSpeedNumber = averageSpeed.toDouble()
        peakAccelerationNumber = peakAcceleration.toDouble()
        finalDistanceNumber = finalDistance.toDouble()
    }
    fun initTimeStamp(): LocalDateTime {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")) //could throw error! did so during init so commented out this.parsedDateTime
    }

}