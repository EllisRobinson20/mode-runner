package com.college.moderunnermvp

import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.database.getDoubleOrNull
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.synthetic.main.activity_speedometer.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.sql.Date
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


class Speedometer : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, TimeTracker.OnFragmentInteraction {

    val devMode = false
    var db: SQLiteDatabase? = null

    var Tag: String = "Speedometer Activity"

    var timeTrackerFragment: TimeTracker = TimeTracker()
    var settingsFragment: FragmentSettings = FragmentSettings()
    var homeFragment: FragmentHome = FragmentHome()
    var timeTrackerTag: String = "time_tracker_tag"
    var settingsTag: String = "settings_tag"

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(this).get(SharedMessage::class.java)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        createDB()
        showHomePage()
        //Bottom navigation listeners
        bottom_app_nav.setOnNavigationItemReselectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> {
                    tab_layout.visibility = View.VISIBLE
                    var stackCount : Int = this.supportFragmentManager.backStackEntryCount
                    for (i in 0..stackCount)
                        supportFragmentManager.popBackStackImmediate()
                    showHomePage()
                }
                R.id.nav_speed -> {
                    tab_layout.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_settings, timeTrackerFragment, timeTrackerTag)
                        .addToBackStack(null)
                .commit()
                }
                R.id.nav_heart -> Toast.makeText(this, "not available in prototype", Toast.LENGTH_SHORT).show()
                R.id.nav_settings ->{
                    tab_layout.visibility = View.INVISIBLE
                    supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_settings, settingsFragment, settingsTag)
                    .addToBackStack(null)
                    .commit() }
            }
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun createDB() {
        if (devMode)
            applicationContext.deleteDatabase("db_history")
        db = openOrCreateDatabase("db_history", Context.MODE_PRIVATE, null)
        db!!.execSQL("CREATE TABLE IF NOT EXISTS user (height INT, weight INT, waist INT)")
        db!!.execSQL("CREATE TABLE IF NOT EXISTS history (date STRING, targetDistance INT, finishTime REAL, topSpeed REAL, averageSpeed REAL, peakAcceleration Real, finalDistance FLOAT, latLongArray STRING)")

        var cursor: Cursor = db!!.rawQuery("select * from history",null)
        if (cursor.moveToFirst()) {
            while (cursor.moveToNext()) {
                var date = cursor.getString(0)
                var targetDistance = cursor.getString(1)
                var finishTime = cursor.getString(2)
                var topSpeed = cursor.getString(3)
                var averageSpeed = cursor.getString(4)
                var peakAcceleration = cursor.getString(5)
                var finalDistance = cursor.getString(6)
                //var latLngString = cursor.getString(7) not in use

                val dataSample:DataSample = DataSample(date,targetDistance,finishTime,topSpeed,averageSpeed,peakAcceleration,finalDistance)
                model.HistoryFragmentModel.addElement(dataSample)
                Log.i(Tag, model.HistoryFragmentModel.lastElement().date)
            }
        }
    }
    override fun onResume() {
        Log.i(Tag, "onResume")
        super.onResume()
    }
    override fun onAttachFragment(fragment: Fragment) {
        Log.i(Tag, "Fragment attached: $fragment")
        super.onAttachFragment(fragment)
    }
    override fun onPause() {
        Log.i(Tag, "onPause")
        super.onPause()
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
    private fun showHomePage() {
        val adapter = FragmentAdapter(supportFragmentManager)
        var view_pager = this.findViewById<ViewPager>(R.id.view_pager)
        view_pager?.adapter = adapter

        tab_layout.setupWithViewPager(view_pager)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onFragmentInteraction(someText: String) {
        Log.i("Speedometer", "OnFragmentInteraction")
        var acceleration: Double = 0.0
        if (someText=="SaveState") {
            Log.i("Speedometer", "SaveState True")
            for (gpsSample in model.SpeedometerFragmentModel) {
                if (gpsSample.accelerationFrame > acceleration)
                    acceleration = gpsSample.accelerationFrame
            }
            //Save to database here

            var latLongList = arrayListOf<LatLng>()
            for (data in model.SpeedometerFragmentModel) {
                data.latLong?.let { latLongList.add(it) }
            }

            var row1: ContentValues = ContentValues()
            row1.put("date", getDateTime())
            row1.put("targetDistance", model.SpeedometerFragmentModel.lastElement().target)
            row1.put("finishTime", model.SpeedometerFragmentModel.lastElement().overallTime)
            row1.put("topSpeed", model.SpeedometerFragmentModel.lastElement().topSpeed)
            row1.put("averageSpeed", model.UIModel().instantaneousAverageSpeed())
            row1.put("peakAcceleration", acceleration)
            row1.put("finalDistance", model.SpeedometerFragmentModel.lastElement().totalDistance)
            row1.put("latLongArray", latLongList.toString())
            db!!.insert("history",null, row1)
            db!!.close()
        }
    }
    @RequiresApi(Build.VERSION_CODES.O)
    private fun getDateTime(): String? {
        var current = LocalDateTime.now()
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
        var formatted = current.format(formatter)
        return formatted
    }

}