package com.college.moderunnermvp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.github.anastr.speedviewlib.Gauge
import com.github.anastr.speedviewlib.Speedometer
import com.github.anastr.speedviewlib.components.Section
import com.github.anastr.speedviewlib.components.Style
import kotlinx.android.synthetic.main.fragment_dial_gauge.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [DialGaugeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class DialGaugeFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_dial_gauge, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        var colorAccent = this.context.let { ContextCompat.getColor(it!!, R.color.colorAccent) }
        var colorOnPrimary = this.context.let { ContextCompat.getColor(it!!, R.color.colorOnPrimary)}
        var colorOnSurface = this.context.let { ContextCompat.getColor(it!!, R.color.colorOnSurface)}
        //accelerationView.speedometerMode = Speedometer.Mode.TOP_LEFT
        accelerationView.textColor = 0
        accelerationView.unit = "ms\u00B2"
        accelerationView.speedTextColor = colorOnPrimary
        accelerationView.unitTextColor = colorOnPrimary
        accelerationView.clearSections()
        accelerationView.addSections(
            Section(0f,.445f,colorOnSurface,16f, Style.ROUND),
            Section(.445f,.625f,colorAccent,16f, Style.ROUND),
            Section(.625f, 1f, colorOnPrimary, 16f, Style.ROUND)
        )
        accelerationView.minSpeed = 0f
        accelerationView.maxSpeed = 20f
        if (!model.SpeedometerFragmentModel.isEmpty())
            accelerationView.speedTo(model.SpeedometerFragmentModel.lastElement().speedFrame.toFloat(), 1)

        topSpeedView.textColor = 0
        topSpeedView.unit = "ms"
        topSpeedView.speedTextColor = colorOnPrimary
        topSpeedView.unitTextColor = colorOnPrimary
        topSpeedView.clearSections()
        topSpeedView.addSections(
            Section(0f,.445f,colorOnSurface,16f, Style.ROUND),
            Section(.445f,.625f,colorAccent,16f, Style.ROUND),
            Section(.625f, 1f, colorOnPrimary, 16f, Style.ROUND)
        )
        topSpeedView.minSpeed = 0f
        topSpeedView.maxSpeed = 20f


        //currentSpeedView.withTremble = false
        currentSpeedView.textColor = 0
        currentSpeedView.speedTextPosition = Gauge.Position.CENTER
        currentSpeedView.speedometerMode = Speedometer.Mode.TOP
        currentSpeedView.unit = "ms"
        currentSpeedView.speedTextColor = colorOnPrimary
        currentSpeedView.unitTextColor = colorOnPrimary
        currentSpeedView.clearSections()
        currentSpeedView.addSections(
            Section(0f,.445f,colorOnSurface,16f, Style.ROUND),
            Section(.445f,.625f,colorAccent,16f, Style.ROUND),
            Section(.625f, 1f, colorOnPrimary, 16f, Style.ROUND)
        )
        currentSpeedView.minSpeed = 0f
        currentSpeedView.maxSpeed = 20f
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment DialGaugeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DialGaugeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}