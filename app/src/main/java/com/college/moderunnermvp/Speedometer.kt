package com.college.moderunnermvp

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.RadioButton
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import kotlinx.android.synthetic.main.activity_speedometer.*
import kotlinx.android.synthetic.main.activity_speedometer.bottom_app_nav
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class Speedometer : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks, FragmentSettings.OnFragmentInteractionListener, TimeTracker.OnFragmentInteractionListener2 {
    var distanceToRun: ArrayList<Int> = ArrayList()
    var listView: ListView? = null
    var adapter: ArrayAdapter<Int>? = null
    var Tag: String = "DistanceToRun"

    var timeTrackerFragment: TimeTracker = TimeTracker()
    var settingsFragment: FragmentSettings = FragmentSettings()
    var timeTrackerTag: String = "time_tracker_tag"
    var settingsTag: String = "settings_tag"
    var msgFromSettingsFrag : Int? = null
    var msgFromTimeTracker : Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        listView = findViewById(R.id.main_listview)
        loadListView()

        /*if (savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.time_tracker,timeTrackerFragment, timeTrackerTag)
                .commit()
        }*/

        bottom_app_nav.setOnNavigationItemReselectedListener { item: MenuItem ->
            when (item.itemId) {
                R.id.nav_home -> Toast.makeText(this, "speed button selected", Toast.LENGTH_SHORT).show()
                R.id.nav_speed -> {
                   // timeTrackerFragment = TimeTracker.newInstance(msgFromSettingsFrag.toString(),"")
                    supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_settings, timeTrackerFragment, timeTrackerTag)
                        .addToBackStack(null)
                .commit() }
                R.id.nav_heart -> Toast.makeText(this, "heart button selected", Toast.LENGTH_SHORT).show()
                R.id.nav_settings ->{
                   // settingsFragment = FragmentSettings.newInstance(msgFromTimeTracker.toString(), "")
                    supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_settings, settingsFragment, settingsTag)
                    .addToBackStack(null)
                    .commit() }
            }
        }
    }

    override fun onPause() {
        Log.i(Tag, "onPause")
        super.onPause()
    }
    /*fun onStartClick(view: View) {
        val textReceived = input.text.toString().toInt()
        distanceToRun.add(textReceived)
        adapter?.notifyDataSetChanged()
        startSpeedometerService(textReceived)
    }*/
    private fun loadListView() {
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, distanceToRun)
        listView?.adapter = adapter
    }
    /*fun startSpeedometerService(text:Int) {
        Log.i("App", "startSpeedometerMode")
        val intent = Intent(this, SpeedometerService::class.java)
        intent.putExtra(Tag,text)
        startService(intent)
    }*/
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.bottom_nav_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
            R.id.nav_home -> Toast.makeText(this, "speed button selected", Toast.LENGTH_SHORT).show()
            R.id.nav_speed ->  supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_settings, timeTrackerFragment, timeTrackerTag)
            .commit()
            R.id.nav_heart -> Toast.makeText(this, "heart button selected", Toast.LENGTH_SHORT).show()
            R.id.nav_settings -> supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_settings, settingsFragment, settingsTag)
                .commit()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onFragmentInteraction(someNumber: Int) {
        msgFromSettingsFrag = someNumber
        Toast.makeText(this, someNumber.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onFragmentInteraction2(someNumber: Int) {
        msgFromTimeTracker = someNumber
        Toast.makeText(this, "Timer Tracker Fragment data", Toast.LENGTH_SHORT).show()
    }


}