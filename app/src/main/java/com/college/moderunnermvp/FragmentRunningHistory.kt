package com.college.moderunnermvp

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.toColorInt
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_running_history.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentRunningHistory.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentRunningHistory : Fragment(), OnChartGestureListener, OnChartValueSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null


    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }



    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_running_history, container, false)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentRunningHistory.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentRunningHistory().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        var colorAccent = this.context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        var colorOnPrimary = this.context?.let {ContextCompat.getColor(it, R.color.colorOnPrimary)}

        var line_chart =  activity?.findViewById<LineChart>(R.id.line_chart)

        line_chart?.onChartGestureListener = this
        line_chart?.setOnChartValueSelectedListener(this)
        line_chart?.isDragEnabled = true
        line_chart?.setScaleEnabled(false)
        line_chart?.xAxis?.textColor = colorOnPrimary!!
        line_chart?.axisLeft?.textColor = colorOnPrimary
        line_chart?.axisRight?.textColor = colorOnPrimary



        var colorBlackGlass = this.context?.let { ContextCompat.getColor(it, R.color.colorBlackGlass) }
        line_chart?.setBackgroundColor(colorBlackGlass!!)
        var colorOnSurface = this.context?.let { ContextCompat.getColor(it, R.color.colorOnSurface) }
        colorOnSurface?.let { line_chart?.setGridBackgroundColor(it) }
        var d = Description()
        d.text = "Run History"
        if (colorOnSurface != null) {
            d.textColor = colorOnSurface
            d.textSize = 15f
        }
        line_chart?.description = d

        line_chart?.invalidate()
        var yValues = ArrayList<Entry>()
        // for each gpsData object -> get the speed reading and the total time frame
        // speed reading will be the yValues. the constant time value will be the xValues
        yValues.add(Entry(0f, 70f))
        yValues.add(Entry(1f, 80f))
        yValues.add(Entry(2f, 90f))
        yValues.add(Entry(3f, 80f))
        /*for (data in model.SpeedometerFragmentModel) {
            yValues.add(Entry(data.totalDistance.toFloat(), data.speedFrame.toFloat()))
        }*/
        var set1 = LineDataSet(yValues, "Data Set 1")
        set1.fillAlpha = 110


        if (colorAccent != null) {
            set1.color = colorAccent
        }
        set1.lineWidth = 3f

        if (colorOnPrimary != null) {
            set1.valueTextColor = colorOnPrimary
        }
        set1.valueTextSize = 10f

        Log.i("SETDATA", "$set1")
        var dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)
        Log.i("SETDATA", "$dataSet")
        var data = LineData(dataSet)
        Log.i("SETDATA", "${data.dataSets}")
        line_chart?.data = data
        Log.i("SETDATA", "${data.dataSets}")
        super.onResume()
    }
    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
    }

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
    }

    override fun onChartLongPressed(me: MotionEvent?) {
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
    }

    override fun onNothingSelected() {
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
    }
}