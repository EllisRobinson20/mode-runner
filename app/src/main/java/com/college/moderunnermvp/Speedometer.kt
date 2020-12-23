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
    var homeTag: String = "home_tag"
    //var msgFromSettingsFrag : String? = null // dont use these as the view model takes care of sharing state and data
    //var msgFromTimeTracker : String? = null

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
        db!!.execSQL("CREATE TABLE IF NOT EXISTS history (date STRING, targetDistance INT, finishTime REAL, topSpeed REAL, averageSpeed REAL, peakAcceleration Real, finalDistance FLOAT)")

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

    /*override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu,menu) ////// ADD AN OPTIONS MENU HERE
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> supportFragmentManager.beginTransaction()
                .add(R.id.fragment_settings, homeFragment, homeTag)
                .addToBackStack(null)
                .commit()
            R.id.nav_speed ->  supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_settings, timeTrackerFragment, timeTrackerTag)
            .commit()
            R.id.nav_heart -> Toast.makeText(this, "not available in prototype", Toast.LENGTH_SHORT).show()
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_settings, settingsFragment, settingsTag)
                .commit()
        } // in here add share , save, share or other buttons
        return super.onOptionsItemSelected(item)
    }*/

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

            var row1: ContentValues = ContentValues()
            row1.put("date", getDateTime())
            //Log.i(Tag, "")
            row1.put("targetDistance", model.SpeedometerFragmentModel.lastElement().target)
            row1.put("finishTime", model.SpeedometerFragmentModel.lastElement().overallTime)
            row1.put("topSpeed", model.SpeedometerFragmentModel.lastElement().topSpeed)
            row1.put("averageSpeed", model.UIModel().instantaneousAverageSpeed())
            row1.put("peakAcceleration", acceleration)
            row1.put("finalDistance", model.SpeedometerFragmentModel.lastElement().totalDistance)
            db!!.insert("history",null,row1)
            /*var cursor: Cursor = db!!.rawQuery("select * from history",null)

            cursor.moveToLast()
            var time2 = cursor.getString(0)
            val parts = time2.split(" ")
            var first = parts[0]
            var p = null as LocalDateTime
            p = LocalDateTime.parse(time2, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"))
            //var time3 = cursor.getString(6).toFloat()

            cursor.close()*/
            db!!.close()
            //Log.i("Speedomoeter", "time values: $p")
            //at some point in the lifecycle the the view model should take a list of objects which store the history
                //then within the right fragment, these data objects can be aggregated
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