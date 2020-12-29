package com.college.moderunnermvp

import android.app.AlertDialog
import android.content.*
import android.os.Bundle
import android.os.CountDownTimer
import android.os.SystemClock
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.fragment_dial_gauge.*
import kotlinx.android.synthetic.main.fragment_dial_numbers.*
import kotlinx.android.synthetic.main.fragment_summary.*
import kotlinx.android.synthetic.main.fragment_time_tracker.*
import java.lang.RuntimeException
import java.math.RoundingMode
import java.text.DecimalFormat


class TimeTracker : Fragment(), View.OnClickListener {

    val numberFormat = DecimalFormat("#.##")
    private var serviceState: Boolean? = true//determine if the service is running or not

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }
    private var listener: OnFragmentInteraction? = null

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
    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteraction) {
            listener=context
        } else {
            throw RuntimeException(context.toString()+"must implement OnFragmentListener")
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
        var tabLayout = view?.findViewById<TabLayout>(R.id.tab_layout)
        tabLayout?.visibility = View.INVISIBLE
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
        if (!model.SpeedometerFragmentModel.isEmpty()) {
            numbers_acceleration?.text =
                numberFormat.format(model.SpeedometerFragmentModel.lastElement().accelerationFrame)
            top_speed?.text =
                numberFormat.format(model.SpeedometerFragmentModel.lastElement().topSpeed)
            numbers_speed?.text =
                numberFormat.format(model.SpeedometerFragmentModel.lastElement().speedFrame)
            txt_distance_remaining?.text = numberFormat.format(
                model.SpeedometerFragmentModel.lastElement().distanceRemaining()
            )
            txt_distance_covered?.text =
                numberFormat.format(model.SpeedometerFragmentModel.lastElement().totalDistance)
            txt_average_speed?.text = numberFormat.format(model.UIModel().averageSpeed())
            var elapsedTime: Double = getElapsedTime()
            chronometer?.base = SystemClock.elapsedRealtime() - (0*6000+elapsedTime.toLong()*1000)
        }
        /*if (!model.SpeedometerFragmentModel.isEmpty()) {
            txt_distance_covered.text = model.SpeedometerFragmentModel.lastElement().totalDistance.toString()
            txt_distance_remaining.text = model.SpeedometerFragmentModel.lastElement().distanceRemaining().toString()
            //Average here speed when calculated
            var elapsedTime : Long = 0
            for ((c) in (0..model.SpeedometerFragmentModel.size).withIndex()) {
                elapsedTime += model.SpeedometerFragmentModel.elementAt(c-1).timeFrame.toLong()
            }
            chronometer.base = elapsedTime
        }*/ // attempt to solve view being destroyed and view not re assigneming
        super.onResume()
        var fadeIn: Animation = AnimationUtils.loadAnimation(activity, R.anim.fade_in)
        prompter_label.startAnimation(fadeIn)

        if (model.SettingsFragmentMsg == null || model.SettingsFragmentMsg == "")  {
            prompter_label.text = "Set Distance First"
        } else
            prompter_label.text = "Ready!"

        val adapter = SpeedViewAdapter(this.childFragmentManager)
        var view_pager = activity!!.findViewById<ViewPager>(R.id.dial_view_pager)
        view_pager?.adapter = adapter


        //summary_tab_layout.setupWithViewPager(dial_view_pager)
    }

    override fun onDestroyView() {
        Log.i("TimeTracker", "On Destroy View")
        if (serviceState == false)
            listener?.onFragmentInteraction("SaveState")
        super.onDestroyView()
    }
    override fun onDetach() {
        serviceState = true
        super.onDetach()
        listener = null
    }

    private fun getElapsedTime(): Double {

        var elapsedTime : Double = 0.00
        for ((c) in (1..model.SpeedometerFragmentModel.size).withIndex()) {
            elapsedTime += model.SpeedometerFragmentModel.elementAt(c).timeFrame
            Log.i("ELAPSEDTIME TIMETRACKER", elapsedTime.toString())
        }
        return elapsedTime
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

    /*private fun sendToActivity(stopRequest: String) {
        var intent = Intent("STOP_REQUEST")
        intent.putExtra("STOP_REQUEST", stopRequest)

        LocalBroadcastManager.getInstance(this.activity!!.application).sendBroadcast(intent)
    }*/

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
            if (model.SpeedometerFragmentModel.size>2)//view cannot run the operation without at least 2 sets of data
                txt_average_speed.text = numberFormat.format(model.UIModel().averageSpeed())
            model.SpeedometerFragmentModel.add(gpsSample)

            var serviceUpdate: Boolean? = gpsSample?.serviceIsRunning
            serviceState = serviceUpdate
            if (serviceState == false||gpsSample!!.serviceComplete)
                stopStartButton(gpsSample!!.serviceComplete)
            Log.i("MODELVIEW SIZE", model.SpeedometerFragmentModel.size.toString())
            Log.i("SERVICERUNNING BOOLEAN", gpsSample?.serviceIsRunning.toString())

            Log.d("BROADCASTRECEIVER", "Broadcast received")
            accelerationView.speedTo(model.SpeedometerFragmentModel.lastElement().accelerationFrame.toFloat(), 1)
            topSpeedView.speedTo(model.SpeedometerFragmentModel.lastElement().topSpeed.toFloat(), 1)
            currentSpeedView.speedTo(model.SpeedometerFragmentModel.lastElement().speedFrame.toFloat(), 1)
            //currentSpeedView.withTremble = true
        }
    }

    private fun stopStartButton(isComplete:Boolean) {
        startButton.text = "Start"
        chronometer.stop()
        button_reset.visibility = View.VISIBLE
        if (isComplete) {
            showRunSummary()
        }
        else {
            serviceStoppedDialog()
        }


    }

    private fun toggleStartButton() {

        if (startButton.text == "Start" && serviceState == true)
        {
            if (chronometer.timeElapsed > 1e-9) {
                chronometer.base = SystemClock.elapsedRealtime()

                chronometer.start()
                chronometer.base = SystemClock.elapsedRealtime() - (0*6000 + getElapsedTime().toLong()*1000)
            }
           /* var elapsedTime: Double = getElapsedTime()
            chronometer.base = SystemClock.elapsedRealtime() - (0*6000+elapsedTime.toLong()*1000)
                chronometer.base =*/

            else
                timer.start()// start countdown in prompter then start the service
            startButton.text = "Stop"
            button_reset.isEnabled = true

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
            button_reset.isEnabled = false
        }
        else
        {
            chronometer.stop()
            startButton.text = "Start"
            Toast.makeText(activity, "Pausing ...", Toast.LENGTH_SHORT).show()
            //sendToActivity("false")
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
    private fun showRunSummary()
    {
        // show a summary of run data
        val fragmentSummary = FragmentSummary()
        val fragmentManager = activity!!.supportFragmentManager
        val fragmentTransaction =fragmentManager.beginTransaction()
        //fragmentTransaction.remove(this)
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
        fragmentTransaction.replace(R.id.fragment_summary, fragmentSummary)
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()

    }
    interface OnFragmentInteraction {
        fun onFragmentInteraction(someText: String)
    }
}