package com.college.moderunnermvp

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import java.text.DecimalFormat

//The updateDuration in speedometer deals with converting time samples to seconds
class GpsSample(
    var distanceFrame: Float,
    var timeFrame: Double, val topSpeed: Double, val target: Int, val totalDistance: Double, val serviceIsRunning: Boolean):
    Parcelable
{

    var speedFrame = 0.0
    var accelerationFrame = 0.0

    init {
        speedFrame = distanceFrame.div(timeFrame)
        accelerationFrame = distanceFrame.div((timeFrame)*(timeFrame))
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readBoolean()
    ) {
    }


    fun isFaster(): Boolean {
        return speedFrame > topSpeed
    }
    fun distanceRemaining(): Double {
        return target.toDouble() - totalDistance
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(distanceFrame)
        parcel.writeDouble(timeFrame)
        parcel.writeDouble(topSpeed)
        parcel.writeInt(target)
        parcel.writeDouble(totalDistance)
        parcel.writeBoolean(serviceIsRunning)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GpsSample> {
        @RequiresApi(Build.VERSION_CODES.Q)
        override fun createFromParcel(parcel: Parcel): GpsSample {
            return GpsSample(parcel)
        }

        override fun newArray(size: Int): Array<GpsSample?> {
            return arrayOfNulls(size)
        }
    }
}