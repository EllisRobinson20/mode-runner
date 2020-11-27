package com.college.moderunnermvp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.ListView
import kotlinx.android.synthetic.main.activity_speedometer.*

class Speedometer : AppCompatActivity() {
    var distanceToRun: ArrayList<Int> = ArrayList()
    var listView: ListView? = null
    var adapter: ArrayAdapter<Int>? = null
    var tag: String = "DistanceToRun"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer)
        listView = findViewById(R.id.main_listview)
        loadListView()
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
        intent.putExtra(tag,text)
        startService(intent)
    }


}