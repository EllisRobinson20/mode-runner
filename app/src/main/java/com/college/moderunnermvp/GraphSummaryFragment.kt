package com.college.moderunnermvp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.listener.ChartTouchListener
import com.github.mikephil.charting.listener.OnChartGestureListener
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import kotlinx.android.synthetic.main.fragment_graph_summary.*
import kotlinx.android.synthetic.main.fragment_graph_summary.title_chart_1
import kotlinx.android.synthetic.main.fragment_graph_summary.title_chart_2
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
class GraphSummaryFragment : Fragment(), OnChartGestureListener, OnChartValueSelectedListener {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var dTag: String = "GraphSummaryFragment"


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
        Log.d(dTag, "OnResume: testing view. Showing the timeframe model ${model.SpeedometerFragmentModel.lastElement().timeFrame}")
        createLineChart(R.id.line_chart, "Time")
        createLineChart(R.id.line_chart_distance, "Distance")
        super.onResume()
    }

    private fun createLineChart(lineChart: Int, chart: String) {
        title_chart_2.text = "Distance"
        title_chart_1.text = "Time"
        var colorAccent = this.context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
        var colorOnPrimary = this.context?.let {ContextCompat.getColor(it, R.color.colorOnPrimary)}

        var summaryChart: LineChart = view!!.findViewById(lineChart)


        //lets check this line_chart to make sure it has been found
        //also the dummy data taking n yValues not working, check git code

        //summaryChart?.onChartGestureListener = this
        //summaryChart?.setOnChartValueSelectedListener(this)
        summaryChart?.isDragEnabled = true
        summaryChart?.setScaleEnabled(false)
        summaryChart?.xAxis?.textColor = colorOnPrimary!!
        summaryChart?.axisLeft?.textColor = colorOnPrimary
        summaryChart?.axisRight?.textColor = colorOnPrimary
        summaryChart?.legend?.textColor = colorOnPrimary

        summaryChart?.axisRight?.isEnabled = false
        summaryChart?.xAxis?.position = XAxis.XAxisPosition.BOTH_SIDED

        //Log.i("createLineChart", model.UIModel().averageSpeed().toString())

        var label = "Average Speed"
        if (!model.SpeedometerFragmentModel.isEmpty()) {
            Log.d(dTag, "If block 158 model has values ${model.SpeedometerFragmentModel.lastElement().timeFrame.toString()}")
            //var yPosition = if (model.SpeedometerFragmentModel.lastElement().topSpeed.toFloat()>0) model.SpeedometerFragmentModel.lastElement().topSpeed.toFloat() else 0f
            var avgSpeedYPosition = model.UIModel().instantaneousAverageSpeed().toFloat()
            Log.i("instantaneousAVG", model.UIModel().instantaneousAverageSpeed().toString())
            var upperLimit: LimitLine = LimitLine(avgSpeedYPosition, label)
            upperLimit.lineWidth = 4f
            upperLimit.enableDashedLine(10f,10f,0f)
            upperLimit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
            upperLimit.textSize = 15f
            upperLimit.textColor = colorOnPrimary
            upperLimit.lineColor = colorOnPrimary!!

            /*    var leftAxis = line_chart?.axisLeft
            leftAxis?.removeAllLimitLines()
            leftAxis?.addLimitLine(upperLimit)*/
            var leftAxis: YAxis = summaryChart?.axisLeft ?: YAxis()
            leftAxis.removeAllLimitLines()
            leftAxis.addLimitLine(upperLimit)
            leftAxis.setDrawLimitLinesBehindData(true)
            leftAxis.enableGridDashedLine(10f, 10f, 0f)
        }



        var colorBlackGlass = this.context?.let { ContextCompat.getColor(it, R.color.colorBlackGlass) }
        summaryChart?.setBackgroundColor(colorBlackGlass!!)
        var colorOnSurface = this.context?.let { ContextCompat.getColor(it, R.color.colorOnSurface) }
        colorOnSurface?.let { summaryChart?.setGridBackgroundColor(it) }
        var d = Description()
        d.text = "Run History"
        if (colorOnSurface != null) {
            d.textColor = colorOnSurface
            d.textSize = 15f
        }
        summaryChart?.description = d

        summaryChart?.invalidate()
        var speedValues = ArrayList<Entry>()
        var accelerationValues = ArrayList<Entry>()
        var timeValues = ArrayList<Entry>()
        var compoundedTime = 0.0
        fun rtnIf(): GpsSample? {
            return if (model.SpeedometerFragmentModel.size>1)
                model.SpeedometerFragmentModel.firstElement()
            else
                null

        }
        var previousObject = rtnIf()
        // for each gpsData object -> get the speed reading and the total time frame
        // speed reading will be the yValues. the constant time value will be the xValues
        /*speedValues.add(Entry(0f, 70f))
        speedValues.add(Entry(1f, 80f))
        speedValues.add(Entry(2f, 90f))
        speedValues.add(Entry(3f, 80f))*/
        if (chart == "Distance") {
            Log.d(dTag, "If block testing for distance")
            for (data in model.SpeedometerFragmentModel) {
                Log.d(dTag, "Inside Distance for loop")
                speedValues.add(Entry(data.totalDistance.toFloat(), data.speedFrame.toFloat()))
                if (data.accelerationFrame > 100)
                    accelerationValues.add(Entry(data.totalDistance.toFloat(), previousObject!!.accelerationFrame.toFloat()))
                else
                    accelerationValues.add(Entry(data.totalDistance.toFloat(), data.accelerationFrame.toFloat()))
                if (data.totalDistance != model.SpeedometerFragmentModel.lastElement().totalDistance) {
                    compoundedTime += data.timeFrame.toFloat()
                    timeValues.add(Entry(data.totalDistance.toFloat(), compoundedTime.toFloat()))
                }
                previousObject = data
            }
        }
        else if (chart == "Time") {
            Log.d(dTag, "if block testing for time")
            for (data in model.SpeedometerFragmentModel) {
                Log.d(dTag, "Inside Time for loop")
                speedValues.add(Entry(data.overallTime.toFloat(), data.speedFrame.toFloat()))
                if (data.accelerationFrame > 100)
                    accelerationValues.add(Entry(data.overallTime.toFloat(), previousObject!!.accelerationFrame.toFloat()))
                else
                    accelerationValues.add(Entry(data.overallTime.toFloat(), data.accelerationFrame.toFloat()))


                timeValues.add(Entry(data.overallTime.toFloat(), data.totalDistance.toFloat()))

                previousObject = data// this helps to remove errors in the acceleration test data
            }
        }

        fun rtnTimeDistanceX(string:String): String {
            return if (string == "Time")
                "Distance"
            else
                "Time"
        }
        var set1 = LineDataSet(speedValues, "Speed")
        set1.fillAlpha = 110

        var set2 = LineDataSet(accelerationValues, "Acceleration")
        set2.fillAlpha = 110
        var set3 = LineDataSet(timeValues, rtnTimeDistanceX(chart))
        set3.fillAlpha = 110


        if (colorAccent != null) {
            set1.color = colorAccent
        }
        set1.lineWidth = 3f
        set2.color = colorOnSurface!!
        set2.lineWidth = 3f
        set3.color = colorOnPrimary!!
        set3.lineWidth = 3f

        if (colorOnPrimary != null) {
            set1.valueTextColor = colorOnPrimary
        }
        set1.valueTextSize = 10f
        set2.valueTextColor = colorOnPrimary
        set2.valueTextSize = 10f
        set3.valueTextColor = colorOnPrimary
        set3.valueTextSize = 10f

        Log.i("SETDATA", "$set1")
        var dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)
        dataSet.add(set2)
        dataSet.add(set3)
        Log.i("SETDATA", "$dataSet")
        var data = LineData(dataSet)
        Log.i("SETDATA", "${data.dataSets}")
        summaryChart.data = data
        Log.i("SETDATA", "${data.dataSets}")
    }

    override fun onChartGestureEnd(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
        TODO("Not yet implemented")
    }

    override fun onChartFling(
        me1: MotionEvent?,
        me2: MotionEvent?,
        velocityX: Float,
        velocityY: Float
    ) {
        TODO("Not yet implemented")
    }

    override fun onChartSingleTapped(me: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onChartGestureStart(
        me: MotionEvent?,
        lastPerformedGesture: ChartTouchListener.ChartGesture?
    ) {
        TODO("Not yet implemented")
    }

    override fun onChartScale(me: MotionEvent?, scaleX: Float, scaleY: Float) {
        TODO("Not yet implemented")
    }

    override fun onChartLongPressed(me: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onChartDoubleTapped(me: MotionEvent?) {
        TODO("Not yet implemented")
    }

    override fun onChartTranslate(me: MotionEvent?, dX: Float, dY: Float) {
        TODO("Not yet implemented")
    }

    override fun onNothingSelected() {
        TODO("Not yet implemented")
    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        TODO("Not yet implemented")
    }
}