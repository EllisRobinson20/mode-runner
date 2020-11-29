package com.college.moderunnermvp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import kotlinx.android.synthetic.main.activity_speedometer.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import kotlin.properties.Delegates

class Speedometer : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    var distanceToRun: ArrayList<Int> = ArrayList()
    var listView: ListView? = null
    var adapter: ArrayAdapter<Int>? = null
    var Tag: String = "DistanceToRun"
    val PERMISSION_ID = 42
    var speedometer:Speedometer = this

    private val LOCATION_PERMISSION_CODE = 124

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var isDone: Boolean by Delegates.observable(false){ property, oldValue, newValue ->
        if (newValue==true) {
            fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        listView = findViewById(R.id.main_listview)
        loadListView()


        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askForLocalPosition()
        createLocationRequest()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult ?: return
                if (!isDone) {
                    val speedToInt = locationResult.lastLocation.longitude.toInt()
                    calulateSpeed(speedToInt)
                    ///add the result to view component here
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        startLocationUpdates()
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

    override fun onPause() {
        super.onPause()
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
    }

    private fun calulateSpeed(speedToInt: Int) {
        Log.i(Tag, "calculate speed result : $speedToInt")
    }

    fun onStartClick(view: View) {
        val textReceived = distance_input.text.toString().toInt()
        distanceToRun.add(textReceived)
        adapter?.notifyDataSetChanged()
        startSpeedometerService(textReceived)
    }
    private fun loadListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, distanceToRun)
        listView?.adapter = adapter
    }
    fun startSpeedometerService(text:Int) {
        Log.i("App", "startSpeedometerMode")
        val intent = Intent(this, SpeedometerService::class.java)
        intent.putExtra(Tag,text)
        startService(intent)
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
            EasyPermissions.requestPermissions(
                this,
                "need permissions to find your location and calculate the speed",
                LOCATION_PERMISSION_CODE,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        }
    }

    private fun hasLocationPermissions(): Boolean {
        return EasyPermissions.hasPermissions(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions,grantResults,this)
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE){
            val yes = "Allow"
            val no = "Deny"
            Toast.makeText(this, "onActivityResult",Toast.LENGTH_LONG).show()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onRationaleDenied(requestCode: Int) {

    }

    override fun onRationaleAccepted(requestCode: Int) {
    }

}