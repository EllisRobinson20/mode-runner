package com.college.moderunnermvp

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_speedometer.*
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import java.text.DecimalFormat
import java.time.LocalTime
import javax.crypto.Cipher


class FragmentHome : Fragment() {
    val numberFormat = DecimalFormat("#.##")
    var db: SQLiteDatabase? = null
    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_home, container, false)
    }
    override fun onStart() {

        super.onStart()
        if (model.SettingsFragmentMsg != null || model.SettingsFragmentMsg != "")  {
            target_set.text = model.SettingsFragmentMsg
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        button_go.setOnClickListener { view ->
            var timeTracker: TimeTracker = TimeTracker()
            var timeTrackerTag: String = "time_tracker_tag"
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.time_tracker, timeTracker, timeTrackerTag)
                .addToBackStack(null)
                .commit()
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onResume() {
        super.onResume()
        var id = 0
        if (target_set.text != "")
            id = target_set.text.toString().toDouble().toInt()
        Log.i("onViewCreated::" ,"id is $id")
        db = activity!!.openOrCreateDatabase("db_history", Context.MODE_PRIVATE, null)
        var cursorTime: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $id ORDER BY finishTime ASC LIMIT 1", null)
        var cursorSpeed: Cursor = db!!.rawQuery("SELECT * FROM history WHERE targetDistance = $id ORDER BY topSpeed DESC LIMIT 1", null)
        if (cursorTime.moveToFirst())
            view_time.text = formatTime(cursorTime.getString(2))
        if (cursorSpeed.moveToFirst())
            view_speed.text = numberFormat.format(cursorSpeed.getString(3).toFloat())
        cursorSpeed.close()
        cursorTime.close()
        db!!.close()
    }
    @RequiresApi(Build.VERSION_CODES.O)
    fun formatTime(time:String): String {
        var stringList = time.split(".").map { it -> it.trim()}
        return when (stringList.size) {
            2 -> return LocalTime.of(0, 0, stringList[0].toInt() ,stringList[1].toInt()).toString()
            3 -> return LocalTime.of(0, stringList[0].toInt(), stringList[1].toInt() ,stringList[2].toInt()).toString()
            4 -> return LocalTime.of(stringList[0].toInt(), stringList[1].toInt(), stringList[2].toInt() ,stringList[3].toInt()).toString()
            else -> LocalTime.of(0, 0,0,0).toString()
        }
    }
}