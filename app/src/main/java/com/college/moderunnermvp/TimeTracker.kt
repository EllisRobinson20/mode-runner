package com.college.moderunnermvp

import android.app.AlertDialog
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.nfc.Tag
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_speedometer.*

import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import java.math.RoundingMode
import java.text.DecimalFormat


class TimeTracker : Fragment(), View.OnClickListener {

    val numberFormat = DecimalFormat("#.##")
    private var serviceState: Boolean? = true//determine if the service is running or not

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }

    //var messageReceiver: LocalBroadcastManager = LocalBroadcastManager.getInstance()

    val timer = object: CountDownTimer(7000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var time_seconds = millisUntilFinished / 1000

            when (time_seconds.toInt()) {
                6 -> {  startButton.isEnabled = false
                        prompter_label.text = "OK"
                        prompter_label.textAlignment = View.TEXT_ALIGNMENT_CENTER
                }
                    4 -> prompter_label.text = "ON YOUR MARKS"
                2 -> prompter_label.text = "SET"
                0-> {prompter_label.textSize = 36f
                    prompter_label.text = "GO!"
                    startButton.isEnabled = true
                }
                else -> {}
            }
        }
        override fun onFinish() {
            prompter_label.text = ""
            startSpeedometerService()
            startViewTimer()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        numberFormat.roundingMode = RoundingMode.CEILING
        Log.i("tag", "onCreate")
        super.onCreate(savedInstanceState)
        LocalBroadcastManager.getInstance(this.requireContext()).registerReceiver(
            messageReceiver(), IntentFilter("GPSUpdate")
        )
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_time_tracker, container, false)
        Log.i("tag", "onCreateView")
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_time_tracker, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        startButton.setOnClickListener { view ->
            //startButton.isSelected != startButton.isSelected
            if (model.SettingsFragmentMsg != null && model.SettingsFragmentMsg != ""){
                toggleStartButton()
                if (startButton.text == "Start")

                Log.i("tag","startButton fired: onViewCreate/TimeTracker")
            } else {
                var  dialogBuilder = AlertDialog.Builder(activity)
                var layoutView = layoutInflater.inflate(R.layout.no_input_dialog, null)
                var dialogButton = layoutView.findViewById(R.id.btnDialog) as Button
                dialogBuilder.setView(layoutView)
                var   alertDialog = dialogBuilder.create()
                alertDialog.show()
                dialogButton.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })
            }
        }
        button_reset.setOnClickListener { view ->
            chronometer.base = SystemClock.elapsedRealtime()
            model.SpeedometerFragmentModel.clear()
            Toast.makeText(activity, "Current Data Cleared", Toast.LENGTH_SHORT).show()
        }
        super.onViewCreated(view, savedInstanceState)
    }
    override fun onStart() {
        super.onStart()
            txt_distance_remaining.text = model.SettingsFragmentMsg
    }
    override fun onResume() {
        super.onResume()
        var fadeIn: Animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        prompter_label.startAnimation(fadeIn)

        if (model.SettingsFragmentMsg == null || model.SettingsFragmentMsg == "")  {
            prompter_label.text = "Set Distance First"
        } else
            prompter_label.text = "Ready!"
    }
    fun startSpeedometerService() {
        Log.i("App", "startSpeedometerMode")
        val intent = Intent(activity, SpeedometerService::class.java)
        val textReceived = txt_distance_remaining.text.toString()
        if (txt_distance_remaining != null || txt_distance_remaining.inputType.equals(Int)) {

            intent.putExtra("DistanceToRun",textReceived)
            // Toast.makeText(activity, "variable to add to service: $textReceived", Toast.LENGTH_SHORT).show()
            Log.i("speedometerService", "startSpeedometer service working "+ textReceived)

        }
        activity?.startService(intent)
    }

    override fun onClick(v: View?) {

    }

    private inner class messageReceiver: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            var message: String? = intent?.getStringExtra("Status")

            var bundle: Bundle? = intent?.getBundleExtra("Location")
            var gpsSample = bundle?.getParcelable<GpsSample>("LocationData")
            var target_distance = gpsSample?.target //does this update?
            //Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()

            var tf = gpsSample?.timeFrame
                //Log.i("MESSAGERECEIVER", "the message reads: $tf")

            numbers_acceleration.text = numberFormat.format(gpsSample?.accelerationFrame)
            top_speed.text = numberFormat.format(gpsSample?.topSpeed)
            numbers_speed.text = numberFormat.format(gpsSample?.speedFrame)
            txt_distance_remaining.text = numberFormat.format(gpsSample?.distanceRemaining())
            txt_distance_covered.text = numberFormat.format(gpsSample?.totalDistance)

            model.SpeedometerFragmentModel.add(gpsSample)

            var serviceUpdate: Boolean? = gpsSample?.serviceIsRunning
            serviceState = serviceUpdate
            if (serviceState == false)
                stopStartButton()
            Log.i("MODELVIEW SIZE", model.SpeedometerFragmentModel.size.toString())
            Log.i("SERVICERUNNING BOOLEAN", gpsSample?.serviceIsRunning.toString())
        }
    }

    private fun stopStartButton() {
        startButton.text = "Start"
        chronometer.stop()
        button_reset.visibility = View.VISIBLE
        serviceStoppedDialog()
    }

    private fun toggleStartButton() {

        if (startButton.text == "Start" && serviceState == true)
        {
            if (chronometer.timeElapsed > 1e-9)
                chronometer.start()
            else
                timer.start()// start countdown in prompter then start the service
            startButton.text = "Stop"
            button_reset.visibility = View.INVISIBLE

        }
        else if (startButton.text == "Start" && serviceState == false) {
            Log.i("START AND FALSE", "Fired!!")
            val intent = Intent(activity, SpeedometerService::class.java)
            val textReceived = txt_distance_remaining.text.toString()
            if (txt_distance_remaining != null || txt_distance_remaining.inputType.equals(Int)) {
                intent.putExtra("DistanceToRun",textReceived)
            }
            if (chronometer.timeElapsed > 1e-9) {
                chronometer.start()
                activity?.startService(intent)
            } else {
                timer.start()// start countdown in prompter then start the service
            }
            startButton.text = "Stop"
            button_reset.visibility = View.INVISIBLE
        }
        else
        {
            chronometer.stop()
            startButton.text = "Start"
            Toast.makeText(activity, "Pausing ...", Toast.LENGTH_SHORT).show()
        }
    }
    private fun startViewTimer() {
        //var chronometer = chronometer
        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()
    }

    private fun serviceStoppedDialog() {
        var  dialogBuilder = AlertDialog.Builder(activity)
        var layoutView = layoutInflater.inflate(R.layout.service_stopped_dialog, null)
        var dialogButton = layoutView.findViewById(R.id.btnDialog) as Button
        dialogBuilder.setView(layoutView)
        var   alertDialog = dialogBuilder.create()
        alertDialog.show()
        dialogButton.setOnClickListener(View.OnClickListener { alertDialog.dismiss() })
    }
}