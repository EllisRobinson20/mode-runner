package com.college.moderunnermvp

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.android.gms.maps.model.LatLng
import java.text.DecimalFormat

//The updateDuration in speedometer deals with converting time samples to seconds
class GpsSample(
    var distanceFrame: Float,
    var timeFrame: Double, val overallTime: Double, val topSpeed: Double, val target: Int, val totalDistance: Double, val serviceIsRunning: Boolean, val serviceComplete: Boolean,
    var latLong:LatLng?):
    Parcelable
{

    var speedFrame = 0.0
    var accelerationFrame = 0.0

    init {
        speedFrame = calculateSpeed()
        accelerationFrame = calculateAcceleration()
    }
    @RequiresApi(Build.VERSION_CODES.Q)
    constructor(parcel: Parcel) : this(
        parcel.readFloat(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readInt(),
        parcel.readDouble(),
        parcel.readBoolean(),
        parcel.readBoolean(),
        parcel.readParcelable<LatLng>(ClassLoader.getSystemClassLoader())
    ) {
    }

    fun calculateSpeed():Double {
        return distanceFrame.div(timeFrame)
    }
    fun calculateAcceleration():Double {
        return distanceFrame.div((timeFrame)*(timeFrame))
    }

    fun isFaster(): Boolean {
        return speedFrame > topSpeed
    }
    fun distanceRemaining(): Double {
        return if (target.toDouble()>=totalDistance)
            target.toDouble() - totalDistance
        else
            0.00
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeFloat(distanceFrame)
        parcel.writeDouble(timeFrame)
        parcel.writeDouble(overallTime)
        parcel.writeDouble(topSpeed)
        parcel.writeInt(target)
        parcel.writeDouble(totalDistance)
        parcel.writeBoolean(serviceIsRunning)
        parcel.writeBoolean(serviceComplete)
        parcel.writeParcelable(latLong,flags)
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