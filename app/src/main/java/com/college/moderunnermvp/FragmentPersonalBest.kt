package com.college.moderunnermvp

import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_personal_best.*
import java.time.LocalTime
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentPersonalBest.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentPersonalBest : Fragment() {

    var db: SQLiteDatabase? = null

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.i("PB:" , "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_best, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var statsList: Vector<StatsCard> = Vector(0,1)

        db = activity!!.applicationContext.openOrCreateDatabase("db_history", Context.MODE_PRIVATE, null)
        var cursorDistinct: Cursor = db!!.rawQuery("SELECT DISTINCT targetDistance FROM history ORDER BY targetDistance", null)
        if (cursorDistinct.moveToFirst()) {
            while (cursorDistinct.moveToNext()) {
                var current = cursorDistinct.getString(0)
                var cursorFinishTime: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $current ORDER BY finishTime ASC LIMIT 1", null)
                var cursorTopSpeed: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $current ORDER BY topSpeed DESC LIMIT 1", null)
                var cursorAverageSpeed: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $current ORDER BY averageSpeed DESC LIMIT 1", null)
                var cursorPeakAcceleration: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $current ORDER BY peakAcceleration DESC LIMIT 1", null)
                var targetDistance = cursorDistinct.getString(0)
                var finishTime = ""
                var topSpeed = ""
                var averageSpeed = ""
                var acceleration = ""
                    if (cursorFinishTime.moveToFirst()) {
                    finishTime = cursorFinishTime.getString(2)
                }
                if (cursorTopSpeed.moveToFirst()) {
                    topSpeed = cursorTopSpeed.getString(3)
                } else
                    topSpeed = "No Record"
                    if (cursorTopSpeed.getString(3)== null) {
                    topSpeed = "No Record"
                }
                if (cursorAverageSpeed.moveToFirst() && cursorAverageSpeed.getString(4)!= null) {
                    averageSpeed = cursorAverageSpeed.getString(4)
                } else
                    averageSpeed = "No Record"
                    if (cursorAverageSpeed.getString(4)== null) {
                    averageSpeed = "No Record"
                }
                if (cursorPeakAcceleration.moveToFirst()) {
                    acceleration = cursorPeakAcceleration.getString(5)
                } else
                    acceleration = "No Record"
                    if (cursorPeakAcceleration.getString(5)== null) {
                    acceleration = "No Record"
                }
                var statsCard = StatsCard(targetDistance, topSpeed, acceleration, averageSpeed, finishTime)
                statsList.add(statsCard)
                cursorAverageSpeed.close()
                cursorFinishTime.close()
                cursorPeakAcceleration.close()
                cursorTopSpeed.close()
            }
            var adapter: PbAdapter = PbAdapter(this.requireContext(), statsList)
            var recyclerView: RecyclerView = recycler_view_personal_best
            recyclerView.hasFixedSize()
            recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
            recyclerView.adapter = adapter
        }
        cursorDistinct.close()
        db!!.close()
    }
    inner class StatsCard(val target: String, val topSpeed: String, val acceleration: String, val averageSpeed: String, val finishTime: String) {

        var stringList = listOf<String>()
        init {
            stringList = finishTime.split(".").map { it -> it.trim()}
        }

        @RequiresApi(Build.VERSION_CODES.O)
        fun formatTime(): String {
            return when (stringList.size) {
                2 -> return LocalTime.of(0, 0, stringList[0].toInt() ,stringList[1].toInt()).toString()
                3 -> return LocalTime.of(0, stringList[0].toInt(), stringList[1].toInt() ,stringList[2].toInt()).toString()
                4 -> return LocalTime.of(stringList[0].toInt(), stringList[1].toInt(), stringList[2].toInt() ,stringList[3].toInt()).toString()
                else -> LocalTime.of(0, 0,0,0).toString()
            }
        }
    }

}