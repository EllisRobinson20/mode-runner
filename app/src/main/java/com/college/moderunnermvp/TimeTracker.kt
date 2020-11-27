package com.college.moderunnermvp

import android.app.ActivityManager
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import java.util.jar.Manifest

class TimeTracker : AppCompatActivity() {
    private val RECORD_REQUEST_CODE = 1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_tracker)
    }

    fun startLocationUpdates() {

    }

    fun stopLocationUpdates(view: View) {
        stopLocationService()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == RECORD_REQUEST_CODE && grantResults.size > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService()
            } else  {
                Toast.makeText(this, "Permission denied!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @SuppressWarnings("deprecation")
    fun isLocationServiceRunning(): Boolean  {

        var activityManager: ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service: ActivityManager.RunningServiceInfo in activityManager.getRunningServices(Integer.MAX_VALUE)) {
            if (JDistanceService::class.simpleName.equals(service.service::class.simpleName)) {
            Log.d("D", "service running")
                if (service.foreground) {
                    return false
                }
            }
        }
    return true
    }

    fun startLocationService() {
        if (!isLocationServiceRunning()) run {
            intent = Intent(applicationContext, JDistanceService::class.java)
            intent.action = Constants.ACTION_START_LOCATION_SERVICE
            startService(intent)
            Toast.makeText(this, "Location service started", Toast.LENGTH_SHORT)
        }
    }

    fun stopLocationService() {
        if(isLocationServiceRunning()) {
            intent = Intent(applicationContext, JDistanceService::class.java)
            intent.action = Constants.ACTION_STOP_LOCATION_SERVICE
            stopService(intent)
            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show()
        }
    }

    fun startLocationUpdates(view: View) {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                RECORD_REQUEST_CODE
            )
        } else {
            Log.d("BUTTON_CLICK: ", " Success")
            startLocationService()
        }
    }
}