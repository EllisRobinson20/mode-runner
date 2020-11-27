package com.college.moderunnermvp

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_k_speedometer.*

class KSpeedometer : AppCompatActivity() {
    private val INITIAL_PERMS = arrayOf<String>(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )
    private val INITIAL_REQUEST = 1

    lateinit var locationManager: LocationManager
    private var hasGPS = false
    private var hasNetwork = false
    private var locationGps : Location? = null
    private var locationNetwork : Location? = null

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_k_speedometer)

        setupPermissions()
    }
    private fun setupPermissions() {
        val permission = ContextCompat.checkSelfPermission(this,
            Manifest.permission.ACCESS_FINE_LOCATION)

        if (permission != PackageManager.PERMISSION_GRANTED) {
            Log.i("PERMISSIONS ", "Permission denied")
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onStart() {
        super.onStart()
        enableView()
    }

    private fun disableView() {
        btn_get_location.isEnabled = false
        btn_get_location.alpha = 0.5f
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun enableView() {
        if (!hasGPS || !hasNetwork)
            requestPermissions(INITIAL_PERMS, INITIAL_REQUEST)
        btn_get_location.isEnabled = true
        btn_get_location.alpha = 1f
        btn_get_location.setOnClickListener {getLocation()}
        Toast.makeText(this, "Done", Toast.LENGTH_SHORT).show()
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        Log.d("EVENT FIRE CHECK", "getLocation fired!!")
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        hasGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        hasNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        if (hasGPS || hasNetwork) {

            if (hasGPS) {
                Log.d("LOCATION DATA", "hasGPS")
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 500, 0f, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location!=null) {
                            locationGps = location
                            Log.d("LOCATION DATA", "GPS Latitude : "+ locationGps!!.latitude)
                            Log.d("LOCATION DATA", "GPS Longitude : "+ locationGps!!.longitude)
                            tv_result.append(("\nGps"))
                            tv_result.append(("\nLatitude : " +locationGps!!.latitude))
                            tv_result.append(("\nLongitude : " +locationGps!!.longitude))
                        }
                    }
                })
                val localGpsLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localGpsLocation !=null)
                    locationGps = localGpsLocation
            }
            if (hasNetwork) {
                Log.d("LOCATION DATA", "hasNetwork")
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 500, 0f, object: LocationListener {
                    override fun onLocationChanged(location: Location) {
                        if (location!=null) {
                            locationNetwork = location
                            Log.d("LOCATION DATA", "Network Latitude : "+ locationNetwork!!.latitude)
                            Log.d("LOCATION DATA", "Network Longitude : "+ locationNetwork!!.longitude)
                            tv_result.append(("\nNetwork"))
                            tv_result.append(("\nLatitude : " +locationNetwork!!.latitude))
                            tv_result.append(("\nLongitude : " +locationNetwork!!.longitude))
                        }
                    }
                })
                val localNetworkLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
                if(localNetworkLocation !=null)
                    locationNetwork = localNetworkLocation
            }
            if (locationGps != null && locationNetwork != null) {
                if (locationGps!!.accuracy > locationNetwork!!.accuracy) {
                    tv_result.append(("\nNetwork"))
                    tv_result.append(("\nLatitude : " +locationNetwork!!.latitude))
                    tv_result.append(("\nLongitude : " +locationNetwork!!.longitude))
                    Log.d("LOCATION DATA", "Network Latitude : "+ locationNetwork!!.latitude)
                    Log.d("LOCATION DATA", "Network Longitude : "+ locationNetwork!!.longitude)
                } else {
                    tv_result.append(("\nGps"))
                    tv_result.append(("\nLatitude : " +locationGps!!.latitude))
                    tv_result.append(("\nLongitude : " +locationGps!!.longitude))
                    Log.d("LOCATION DATA", "GPS Latitude : "+ locationGps!!.latitude)
                    Log.d("LOCATION DATA", "GPS Longitude : "+ locationGps!!.longitude)
                }
            }

        } else {
            startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int,
                                            permissions: Array<String>, grantResults: IntArray) {
        when (requestCode) {
            INITIAL_REQUEST -> {

                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Log.i("PERMISSIONS", "Permission has been denied by user")
                } else {
                    Log.i("PERMISSIONS", "Permission has been granted by user")
                }
            }
        }
    }
}