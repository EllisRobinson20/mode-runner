package com.college.moderunnermvp

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.nfc.Tag
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.*
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_speedometer.*

import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*


class TimeTracker : Fragment(), View.OnClickListener {

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }
    val timer = object: CountDownTimer(7000, 1000) {
        override fun onTick(millisUntilFinished: Long) {
            var time_seconds = millisUntilFinished / 1000

            when (time_seconds.toInt()) {
                6 -> prompter_label.text = "OK"
                    4 -> prompter_label.text = "READY"
                2 -> prompter_label.text = "SET"
                0-> prompter_label.text = "GO!"
                else -> {}
            }
        }
        override fun onFinish() {
            prompter_label.text = ""
            startSpeedometerService()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i("tag", "onCreate")
        super.onCreate(savedInstanceState)
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
            if (model.SettingsFragmentMsg != null && model.SettingsFragmentMsg != ""){
                timer.start()// start countdown in prompter then start the service
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
        var chronometer = chronometer as Chronometer
        chronometer.start()
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
            Toast.makeText(activity, "variable to add to service: $textReceived", Toast.LENGTH_SHORT).show()
            Log.i("speedometerService", "startSpeedometer service working "+ textReceived)
        }
        activity?.startService(intent)
    }

    override fun onClick(v: View?) {

    }

}