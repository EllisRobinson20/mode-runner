package com.college.moderunnermvp

import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_stats.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import kotlinx.android.synthetic.main.fragment_time_tracker.txt_distance_covered
import java.text.DecimalFormat

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentStats.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentStats : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    val numberFormat = DecimalFormat("#.##")

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
        return inflater.inflate(R.layout.fragment_stats, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        txt_distance_covered.text = numberFormat.format(model.SpeedometerFragmentModel.lastElement().totalDistance)
        txt_finish_time.start()
        txt_finish_time.base = SystemClock.elapsedRealtime() - (0*6000 + getElapsedTime().toLong()*1000)
        txt_finish_time.stop()
        txt_final_average_speed.text = numberFormat.format(model.UIModel().averageSpeed())
        txt_top_speed.text =  numberFormat.format(model.SpeedometerFragmentModel.lastElement().topSpeed)
        txt_peak_acceleration.text = numberFormat.format(getPeakAcceleration())
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {

        super.onResume()
    }
    private fun getElapsedTime(): Double {

        var elapsedTime : Double = 0.00
        for ((c) in (1..model.SpeedometerFragmentModel.size).withIndex()) {
            elapsedTime += model.SpeedometerFragmentModel.elementAt(c).timeFrame
            Log.i("ELAPSEDTIME TIMETRACKER", elapsedTime.toString())
        }
        return elapsedTime
    }
    private fun getPeakAcceleration(): Double {

        var peak : Double = 0.00
        for ((c) in (1..model.SpeedometerFragmentModel.size).withIndex()) {
            peak += model.SpeedometerFragmentModel.elementAt(c).accelerationFrame
            Log.i("ELAPSEDTIME TIMETRACKER", peak.toString())
        }
        return peak
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentStats.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentStats().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}