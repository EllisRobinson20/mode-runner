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
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.ViewPager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_speedometer.*
import kotlinx.android.synthetic.main.activity_speedometer.bottom_app_nav
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions


class Speedometer : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {


    var Tag: String = "Speedometer Activity"

    var timeTrackerFragment: TimeTracker = TimeTracker()
    var settingsFragment: FragmentSettings = FragmentSettings()
    var homeFragment: FragmentHome = FragmentHome()
    var timeTrackerTag: String = "time_tracker_tag"
    var settingsTag: String = "settings_tag"
    var homeTag: String = "home_tag"
    //var msgFromSettingsFrag : String? = null // dont use these as the view model takes care of sharing state and data
    //var msgFromTimeTracker : String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)



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
}