package com.college.moderunnermvp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_speedometer.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

class Speedometer : AppCompatActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {
    var distanceToRun: ArrayList<Int> = ArrayList()
    var listView: ListView? = null
    var adapter: ArrayAdapter<Int>? = null
    var Tag: String = "DistanceToRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        listView = findViewById(R.id.main_listview)
        loadListView()
    }
    override fun onPause() {
        Log.i(Tag, "onPause")
        super.onPause()
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