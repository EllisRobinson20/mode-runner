package com.college.moderunnermvp

import android.Manifest
import android.app.IntentService
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.core.app.ActivityCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.location.*
import pub.devrel.easypermissions.EasyPermissions
import java.util.*
import kotlin.collections.ArrayList
import kotlin.properties.Delegates


class SpeedometerService : IntentService("Speedometer Service")
{
    var Tag: String = "IntentService"

    var distanceToRun: ArrayList<Int> = ArrayList()
    var listView: ListView? = null
    var adapter: ArrayAdapter<Int>? = null
    //var Tag: String = "DistanceToRun"
    val PERMISSION_ID = 42

    var targetDistance: Int = 0
    var topSpeed: Double = 0.00
    var distances: Vector<Float> = Vector(1,1)
    var locations: Vector<Location> = Vector(1,1)
    var speeds: Vector<Float> = Vector(1, 1)
    var times: Vector<Long> = Vector(1,1)
    var distanceTotal: Double = 0.00
    var distanceRemaining: Double = 0.00

    var result: ArrayList<Float> = ArrayList(0)
    var result_to__add = arrayOf(0.0f,0.0f,0.0f,0.0f,0.0f) // distance , time , speed , acceleration , total distance covered


    var globalCounter: Int = 0


    private val LOCATION_PERMISSION_CODE = 124

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var isDone: Boolean by Delegates.observable(false){ property, oldValue, newValue ->
        if (newValue==true) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onCreate() {
        Log.i(Tag, "onCreate")
        super.onCreate()
        locations.add(0, Location("location $globalCounter"))
        if (times.size == 0) // add this to prevent an indexing error on first array update
            times.add(System.nanoTime())
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(Tag, "onStartCommand")

        targetDistance = intent?.getStringExtra("DistanceToRun")?.toInt()!!
        //Log.i(Tag, "Target distance is: $targetDistance")
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onHandleIntent(intent: Intent?) {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askForLocalPosition()
        createLocationRequest()
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                if (!isDone) {
                    // val speedToDouble = locationResult.lastLocation.speed.toDouble()
                    //calulateSpeed(speedToDouble)
                    ///add the result to view component here
                    updateSession(intent, locationResult ,true)
                    if (distances.size > 30) { //Stop the service if the user does not move locations for 10seconds
                        var sublist = distances.subList(distances.size-15, distances.size)
                        if (sublist.sum() == 0.0f) {
                            //isDone = true
                            updateSession(intent, locationResult, false)
                        }

                    }
                }
            }
        }

        startLocationUpdates()

        //ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)


        /*Log.i(Tag, "onHandleWork")
        Thread.sleep(5000)
        Log.i("Tag","done")
        var distanceInt = intent?.getIntExtra("DistanceToRun", 0)
        var i = distanceInt?.times(2).toString()
        Log.i("Tag",i)*/

    }

    private fun updateSession(intent: Intent?, result: LocationResult, serviceState:Boolean) {
        globalCounter++
        times.add(System.nanoTime())
        updateLocation(result)
        updateDistance(result)
        var currentDuration = updateDuration(result) //instead of: times.lastElement() - times[times.size-2]
        var sample = GpsSample(distances.lastElement(), currentDuration, topSpeed, targetDistance, distanceTotal, serviceState)
        var isFaster = sample.isFaster()
        var distanceRemaining = sample.distanceRemaining()
        //Log.i("SAMPLE CLASS RESULTS","this is faster = $isFaster AND top speed = $topSpeed")
        if (isFaster)
            topSpeed = sample.speedFrame
        this.distanceRemaining = distanceRemaining
        distanceTotal = distances.sum().toDouble()
        //Log.i("SAMPLE CLASS RESULTS","top speed is $topSpeed AND distance remaining = $distanceRemaining AND then theres distance total $distanceTotal")
        //var current: Float = result.lastLocation.distanceTo(location)
        sendToActivity(sample, "GpsSample: ")
        if (!serviceState)
            isDone = true
    }

    private fun updateDuration(result: LocationResult): Double {

        var current_time_sample = times.lastElement()
        var previous_time_sample = times[times.size-2]
        var sample_difference = current_time_sample - previous_time_sample
            //System.currentTimeMillis()
        //times.add(time_sample) now done in update session straight away
        return sample_difference/1e9

        //Log.i(Tag, "Time sampled in updateDuration: +"+ sample_difference/1e9)

    }

    private fun updateDistance(result: LocationResult) {
        /*var distanceFromLat = result.lastLocation.latitude
        var distanceFromLong = result.lastLocation.longitude
        var previousLocation = Location("PreviousLocation")
        previousLocation.latitude = distanceFromLat
        previousLocation.longitude = distanceFromLong*/
        var distance = result.lastLocation.distanceTo(locations[locations.size-2])
        if(distance< 10000)
            distances.add(distance)
        else
            distances.add(0.0f)//ignore errorsome behavior using emulator

        distanceTotal += distances.lastElement()

        result_to__add.set(0,distance)
        result_to__add.set(4, distanceTotal.toFloat())

       /* Log.i(Tag, "DISTANCE" +result_to__add[0])
        Log.i(Tag, "TOTALDISTANCE" +result_to__add[4])
        Log.i(Tag, "DISTANCE ARRAY " +distances)
        Log.i(Tag, "DISTANCE TOTAL " +distanceTotal)*/
    }

    private fun updateLocation(result: LocationResult) {
        var lat = result.lastLocation.latitude
        var long = result.lastLocation.longitude

        var location = Location("Location $globalCounter")
        location.latitude = lat
        location.longitude = long
        location.speed //??
        locations.add(location)
        //Log.i(Tag, "location calculation result : $location")
    }

    private fun sendToActivity(gpsObject: GpsSample, statusMessage: String) {
        var intent: Intent = Intent("GPSUpdate")
        intent.putExtra("Status", statusMessage+globalCounter)
        intent.putExtra("TopSpeed", topSpeed.toString())//top speed is updated outside of the gpsObject
        var bundle = Bundle()
        bundle.putParcelable("LocationData", gpsObject)
        intent.putExtra("Location", bundle)
        LocalBroadcastManager.getInstance(this ).sendBroadcast(intent)
    }


    override fun onDestroy() {
        Log.i(Tag, "onDestroy")

        //fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        super.onDestroy()
    }

    private fun startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        fusedLocationProviderClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }


    private fun createLocationRequest() {
        locationRequest = LocationRequest().apply {
            interval = 0
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }


    fun askForLocalPosition() {
        if (hasLocationPermissions()) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return
            }
            fusedLocationProviderClient.lastLocation
                .addOnSuccessListener { location: Location? ->

                }
        } else {
            /*EasyPermissions.requestPermissions(
                this,
                "need permissions to find your location and calculate the speed",
                LOCATION_PERMISSION_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )*/ // could send request to activity and then paste this cod in there instead???

        }
    }

    private fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private inner class messageReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            Log.i("SPEEDOMETER BRODCAST", "RECEIVED")
            var stopRequest = intent?.getStringExtra("STOP_REQUEST")
            stopRequest?.toBoolean()
            if (!stopRequest?.toBoolean()!!)
                isDone = true
        }

    }
}
