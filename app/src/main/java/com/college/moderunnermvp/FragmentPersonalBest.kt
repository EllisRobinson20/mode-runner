package com.college.moderunnermvp

import android.content.Context
import android.content.res.ColorStateList
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.view.setMargins
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import kotlinx.android.synthetic.main.fragment_personal_best.*
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
        var colorAccent = this.context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        var colorOnPrimary = this.context?.let { ContextCompat.getColor(it, R.color.colorOnPrimary)}
        var statsList: Vector<StatsCard> = Vector(0,1)

        super.onViewCreated(view, savedInstanceState)

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
                }
                if (cursorAverageSpeed.moveToFirst()) {
                    averageSpeed = cursorAverageSpeed.getString(4)
                }
                if (cursorPeakAcceleration.moveToFirst()) {
                    acceleration = cursorPeakAcceleration.getString(5)
                }
                var statsCard = StatsCard(targetDistance, topSpeed, acceleration, averageSpeed, finishTime)
                statsList.add(statsCard)
            }
            var adapter: PbAdapter = PbAdapter(this.requireContext(), statsList)
            var recyclerView: RecyclerView = recycler_view_personal_best
            recyclerView.hasFixedSize()
            recyclerView.layoutManager = LinearLayoutManager(this.requireContext())
            recyclerView.adapter = adapter
        }
    }
    inner class StatsCard(val target: String, val topSpeed: String, val acceleration: String, val averageSpeed: String, val finishTime: String) {
        fun returnCard() : MaterialCardView {
            var colorBlackGlass = context?.let { ContextCompat.getColor(it,R.color.colorBlackGlass) }
            var colorOnPrimary = context?.let { ContextCompat.getColor(it,R.color.colorOnPrimary) }
            var card:MaterialCardView = MaterialCardView(context)
            val paramsTable = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            paramsTable.setMargins(36,36,36,36)
            val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT)
            params.setMargins(16,16,16,16)
            //params.alignWithParent = true
            params.addRule(RelativeLayout.CENTER_IN_PARENT)
            var v = view!!.findViewById<MaterialCardView>(target.toInt())
            var t = view!!.findViewById(R.id.pb_main_layout) as RelativeLayout


            if (t.childCount > 0) {
                var z = t.children.last().id
                Log.i("STATSCARDTEST", z.toString())
                params.addRule(RelativeLayout.BELOW, z )
            }


            card.setCardBackgroundColor(Color.argb(30,12, 12,12))

            card.id = target.toInt()
            card.layoutParams = params
            card.elevation = 3f
            card.radius = 0f

            var table: TableLayout = TableLayout(context)
            table.layoutParams = paramsTable
            var tr1 = TableRow(context)
            var tr2 = TableRow(context)
            var tr3 = TableRow(context)
            var tr4 = TableRow(context)
            var trList = listOf<TableRow>(tr1,tr2,tr3,tr4)

            for (row in trList) {
                row.layoutParams = params
                var title = TextView(context)
                //title.textSize = 24f
                title.setTextColor(colorOnPrimary!!)
                var value = TextView(context)
                var unit = TextView(context)
                if (row == trList.first()) {
                    title.text = "Target"
                    value.text = target.toString()
                    unit.text = resources.getText(R.string.metre)
                }
                if (row == trList.elementAt(1)) {
                    title.text = "Top Speed"
                    value.text = topSpeed.toString()
                    unit.text = resources.getText(R.string.metre_second)
                }
                if (row == trList.elementAt(2)) {
                    title.text = "Average Acceleration"
                    value.text = acceleration.toString()
                    unit.text = resources.getText(R.string.metre_second_squared)
                }
                if (row == trList.last()) {
                    title.text = "Average Acceleration"
                    value.text = averageSpeed.toString()
                    unit.text = resources.getText(R.string.metre_second_squared)
                }
                if (row == trList.last()) {
                    title.text = "Best Time"
                    value.text = finishTime.toString()
                    unit.text = ""
                }

                row.addView(title)
                row.addView(value)
                row.addView(unit)
                table.addView(row)
            }
            card.addView(table)
            return card
        }
    }

}