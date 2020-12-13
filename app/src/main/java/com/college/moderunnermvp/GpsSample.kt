package com.college.moderunnermvp

import android.os.Parcel
import android.os.Parcelable
import java.text.DecimalFormat

//The updateDuration in speedometer deals with converting time samples to seconds
class GpsSample(
    var distanceFrame: Float,
    var timeFrame: Double, val topSpeed: Double, val target: Int, val totalDistance: Double):
    Parcelable
{

    var speedFrame = 0.0
    var accelerationFrame = 0.0

    init {
        speedFrame = distanceFrame.div(timeFrame)
        accelerationFrame = distanceFrame.div((timeFrame)*(timeFrame))
    }
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble()
    ) {
    }


    fun isFaster(): Boolean {
        return speedFrame > topSpeed
    }
    fun distanceRemaining(): Double {
        return target.toDouble() - totalDistance
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(distanceFrame)
        parcel.writeDouble(timeFrame)
        parcel.writeDouble(topSpeed)
        parcel.writeInt(target)
        parcel.writeDouble(totalDistance)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<GpsSample> {
        override fun createFromParcel(parcel: Parcel): GpsSample {
            return GpsSample(parcel)
        }

        override fun newArray(size: Int): Array<GpsSample?> {
            return arrayOfNulls(size)
        }
    }
}