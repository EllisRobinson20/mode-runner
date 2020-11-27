package com.college.moderunnermvp

import android.annotation.SuppressLint
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_speedometer_mode.*
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.*

class SpeedometerMode : AppCompatActivity(), SensorEventListener {
    /*var accelerationX = Array<Double>(2){0.0}
    var accelerationY = Array<Double>(2){0.0}
    var accelerationZ = Array<Double>(2){0.0}

    var velocityX = Array<Double>(2){0.0}
    var velocityY = Array<Double>(2){0.0}
    var velocityZ = Array<Double>(2){0.0}

    var positionX = Array<Double>(2){0.0}
    var positionY = Array<Double>(2){0.0}
    var positionZ = Array<Double>(2){0.0}*/

    //
    var accelerationX: Float? = null
    var accelerationY: Float? = null
    var accelerationZ: Float? = null

    var velocityX: Float? = 0.0f
    var velocityY: Float? = 0.0f
    var velocityZ: Float? = 0.0f

    var positionX: Float? = 0.0f
    var positionY: Float? = 0.0f
    var positionZ: Float? = 0.0f

    var xAccelerations: Vector<Float> = Vector(1, 1)
    var yAccelerations: Vector<Float> = Vector(1, 1)
    var zAccelerations: Vector<Float> = Vector(1, 1)

    var xVelocities: Vector<Float> = Vector(2, 1)
    var yVelocities: Vector<Float> = Vector(2, 1)
    var zVelocities: Vector<Float> = Vector(2, 1)

    var xPositions: Vector<Float> = Vector(2, 1)
    var yPositions: Vector<Float> = Vector(2, 1)
    var zPositions: Vector<Float> = Vector(2, 1)

    var topSpeed: Float? = null
    var topAcceleration: Float? = null

    var targetDistance: Float? = null
    var currentDistance: Float? = null

    var currentTime: Long? = null
    var timeElapsedSeconds: Float? = null
    var timesArray: Vector<Long> = Vector(0,1)
    var df: DecimalFormat = DecimalFormat("#.##" )

    var isCalibrated: Boolean = false
    var checkOk: Boolean = false
    var userIsReady: Boolean = false

    var calibrationCount: Int = 0

    private var sensorManager: SensorManager? = null
    private var sensor: Sensor? = null

    // calibration
    var totalDataX :Float = 0.0f
    var totalDataY :Float = 0.0f
    var totalDataZ :Float = 0.0f

    var xIsDown: Boolean = false
    var yIsDown: Boolean = false
    var zIsDown: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_speedometer_mode)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensor = sensorManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager!!.registerListener(this, sensor, SensorManager.SENSOR_STATUS_ACCURACY_HIGH)
    }

    override fun onStart() {
        super.onStart()

        fun startRun() { // this should maybe be the runnable that provides ui
            if (checkOk) {
                //Do the game stuff here
                Thread.sleep(5000)
               // var speed: Float? = getSpeed() // gets the data of X and Y velocity and aggregates speed


                // now conditions of these variables will drive the game
                // the key is to ue the C++ examples found and to try to convert this into Kotlin equivalent code
                //first need to provide dependencies and permissions to accelerometer

            }
           // else initialCheck()
        }
        fun initialCheck() = when {
            isCalibrated && targetDistance!! > 0.0 -> startRun()
            isCalibrated && targetDistance!! <= 0.0 -> "Please Set a distance!" + TODO("//Send the user to settings activity with a call to onactivityresult to retrieve a distance")
            !isCalibrated -> " was going to call calibrateDevice() but cannot access the accelerometer values this way"
            else -> "Something has gone wrong, please restart the activity"
        }
        accelerationX; accelerationY; accelerationZ; velocityX; velocityY; velocityZ; positionX; positionY; positionZ = 0.00f
        currentDistance; timeElapsedSeconds; topSpeed; topAcceleration = 0.00f
        /*xAccelerations;yAccelerations;zAccelerations;xVelocities;yVelocities;zVelocities.add(0,0.0f)
        xAccelerations;yAccelerations;zAccelerations;xVelocities;yVelocities;zVelocities.add(1,0.0f)*/
        xAccelerations.add(0, 0.0f)
        yAccelerations.add(0, 0.0f)
        zAccelerations.add(0, 0.0f)
        xAccelerations.add(1, 0.0f)
        yAccelerations.add(1, 0.0f)
        zAccelerations.add(1, 0.0f)
        xVelocities.add(0, 0.0f)
        yVelocities.add(0, 0.0f)
        zVelocities.add(0, 0.0f)
        xVelocities.add(1, 0.0f)
        yVelocities.add(1, 0.0f)
        zVelocities.add(1, 0.0f)

        xPositions.add(0, 0.0f)
        yPositions.add(0, 0.0f)
        zPositions.add(0, 0.0f)
        xPositions.add(1, 0.0f)
        yPositions.add(1, 0.0f)
        zPositions.add(1, 0.0f)

       // xAccelerations.add(0, 0.0f)
        Log.i("VALUE CHECK", "${xAccelerations.size}")
        initialCheck()
    }


   /* private fun startRun() {
        TODO("Not yet implemented")
        Thread.sleep(5000)
        TODO("Display a countdown to user")

        var speed: Double = getSpeed() // gets the data of X and Y velocity and aggregates speed
        currentDistance = getDistance()


    }*/

    private fun getDistance(axes: Array<Float>): Float? {

        TODO("Not yet implemented")
        // Double integration of acceleration

    }
    private fun timeElapsed(t:Long): Float {
        var initial_time_millis = timesArray.lastElement()
        var new_time_millis = t
        var time_difference_seconds = (new_time_millis - initial_time_millis)
        Log.i("FUNCTION CHECK ", "before millis convert = $time_difference_seconds" )
        time_difference_seconds
        var d: Double = time_difference_seconds.toDouble()/1000000000.00
        Log.i("FUNCTION CHECK ", "after millis convert = $time_difference_seconds:\n ... The double reads: $d" )
        return d.toFloat()
        // return Math.round((time_difference_seconds.toDouble() *100) / 100).toFloat() /// changed this to the above rtn
        //check these time differences.. main question is cannot resolve infinity
        //but what is the reading of time difference here
        //also added the time below in getSpeed /1000. . justify this
        //check thr algorithm where it does final * of time. is time value wrong(noted above)? is the extra * necessary? is the sum in correct order of operation?
    }

    @SuppressLint("SetTextI18n")
    private fun getSpeed(x:Array<Float>, y:Array<Float>, z:Array<Float>) {
        var t: Long = System.nanoTime()
        var time_difference_seconds = timeElapsed(t)
        timesArray.add((t))
        //Math.round((t/1000*100).toDouble()/100)
        Log.i("TIME CHECK", "latest time in array is: ${timesArray.lastElement()}" + "(getSpeed)")
        Log.i("TIME DIFFERENCE CHECK", "(getSpeed) = $time_difference_seconds")
        //provide velocity for xyz. later we will ignore down axis using booleans
        xVelocities.add((xVelocities.lastElement()+x[0]+((x[1]-x[0])/2))*time_difference_seconds)
        yVelocities.add((yVelocities.lastElement()+y[0]+((y[1]-y[0])/2))*time_difference_seconds)
        zVelocities.add((zVelocities.lastElement()+z[0]+((z[1]-z[0])/2))*time_difference_seconds)
        Log.i("VALUE CHECK" , "Velocity readings: X: "+ xVelocities.lastElement() + " Y: "+ yVelocities.lastElement() + " Z: " + zVelocities.lastElement())
//convert position to positive number
        if (xPositions.lastElement() < 0)
            xPositions[xPositions.size-1] = xPositions.lastElement()*-1
        if (yPositions.lastElement() < 0)
            yPositions[yPositions.size-1] = yPositions.lastElement()*-1
        if (zPositions.lastElement() < 0)
            zPositions[zPositions.size-1] = zPositions.lastElement()*-1
//add x and z to get total distance(hardcoded. Ideally would test for the non vertical axes
        // also note that using two axes could throw greater error in stage 2 tests
        xPositions.set(xPositions.size-1, xPositions.get(xPositions.size-1)+zPositions.get(zPositions.size-1))
       // xPositions.set(xPositions.size-1, xPositions.get(xPositions.size-1)+zPositions.get(zPositions.size-1))

        xPositions.add((xPositions.lastElement()+xVelocities[xVelocities.size-2] +((xVelocities[xVelocities.size-1]-xVelocities[xVelocities.size-2] )/2))*time_difference_seconds)
        yPositions.add((yPositions.lastElement()+yVelocities[yVelocities.size-2] +((yVelocities[yVelocities.size-1]-yVelocities[yVelocities.size-2] )/2))*time_difference_seconds)
        zPositions.add((zPositions.lastElement()+zVelocities[zVelocities.size-2] +((zVelocities[zVelocities.size-1]-zVelocities[zVelocities.size-2] )/2))*time_difference_seconds)
//Position code: adding the positions together to get total distance
        positionX = xPositions.sum()
        positionY = yPositions.sum()
        positionZ = zPositions.sum()
       /* positionX = positionX?.plus(xPositions.lastElement())
        positionY = positionY?.plus(yPositions.lastElement())
        positionZ = positionZ?.plus(zPositions.lastElement())*/
        distanceXText.text = "Distance of X: $positionX"
        distanceYText.text = "Distance of Y: $positionY"
        distanceZText.text = "Distance of Z: $positionZ"

        velocityX = velocityX?.plus(xVelocities.lastElement())
        velocityY = velocityY?.plus(yVelocities.lastElement())
        velocityZ = velocityZ?.plus(zVelocities.lastElement())
        var count_x: Int = 0
        if (xAccelerations.lastElement() ==0.0f)
            count_x++
        else
            count_x = 0
        if (count_x>=25) {
            xVelocities.add(0.0f)
        }
        VelXText.text = "Velocity of X: ${xVelocities.lastElement()}"
        VelYText.text = "Velocity of Y: $velocityY"
        VelZText.text = "Velocity of Z: $velocityZ"


    }

    fun calibrateDevice(event: SensorEvent) {
        Log.i("EVENT CHECKER", "calibration event fired!!!")
        // which axes are parallel to the ground eg perpendicular to gravity
        // three booleans is_xy is_xz is_yz then those are the axes that are read if true in onSensorchanged

        while (calibrationCount < 1024) {
            Log.i("CONDITION CHECKER", "while count not 1025 runs")
            //!! get 1204 samples & add them to vector
            accelerationX = event.values[0]
            accelerationY = event.values[1]
            accelerationZ = event.values[2]
            xPositions.add(calibrationCount, accelerationX)
            yPositions.add(calibrationCount, accelerationY)
            zPositions.add(calibrationCount, accelerationZ)
            calibrationCount++
            // add the data
            if(calibrationCount == 1024) {
                Log.i("CONDITION CHECKER", "if equal to 1024")
                for (data in xPositions) {
                    Log.i("CONDITION CHECKER", "for loop runs")
                    totalDataX += data
                    totalDataY += yPositions[yPositions.size.toInt()-1]
                    totalDataZ += zPositions[zPositions.size.toInt()-1]
                    Log.i("CONDITION CHECKER", "for loop END")
                }
                Log.i("CONDITION CHECKER", "for loop completes")
                //total data divided by number of samples
                Log.i("VALUE CHECK:", "$totalDataX")
                totalDataX /= 1024
                Log.i("VALUE CHECK:", "$totalDataX")
                xPositions.clear()
                xPositions.add(0, 0.0f)
                Log.i("VALUE CHECK:", "$totalDataY")
                totalDataY /= 1024
                Log.i("VALUE CHECK:", "$totalDataY")
                yPositions.clear()
                yPositions.add(0, 0.0f)
                Log.i("VALUE CHECK:", "$totalDataZ")
                totalDataZ /= 1024
                Log.i("VALUE CHECK:", "$totalDataZ")
                zPositions.clear()
                zPositions.add(0, 0.0f)
                Log.i("CALIBRATION", "final reading of x is $totalDataX\n" +
                        "final reading of y is $totalDataY" +
                        "final reading of z is $totalDataZ")
                // decide which axis is vertically aligned
                when {
                    totalDataX > totalDataY && totalDataX > totalDataZ && totalDataX > 0 -> {
                        // X axis points down
                        xIsDown = true
                    }
                    totalDataX < totalDataY && totalDataX < totalDataZ && totalDataX < 0 -> {
                        // X axis points down
                        xIsDown = true
                    }
                    totalDataY > totalDataX && totalDataY > totalDataZ && totalDataY > 0 -> {
                        // Y axis points down
                        yIsDown = true
                    }
                    totalDataY < totalDataX && totalDataY < totalDataZ && totalDataY < 0 -> {
                        // Y axis points down
                        yIsDown = true
                    }
                    totalDataZ > totalDataY && totalDataZ > totalDataX && totalDataX > 0 -> {
                        // Z axis points down
                        zIsDown = true
                    }
                    totalDataZ < totalDataY && totalDataZ < totalDataX && totalDataX < 0 -> {
                        // Z axis points down
                        zIsDown = true
                    }
                }
                Log.i("CONDITION CHECKER", "x is down = $xIsDown... y is down = $yIsDown... z is down = $zIsDown...")
                isCalibrated = true
                Thread.sleep(5000)
                userIsReady = true
                currentTime = System.nanoTime()
                timesArray.add(currentTime)
            }
        }
    }
    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {

    }

    @SuppressLint("SetTextI18n")
    @Suppress("UNREACHABLE_CODE")
    override fun onSensorChanged(event: SensorEvent?) {
        Log.i("EVENT CHECKER", "onSensorFired!!!")
        //Thread.sleep(62)
        when {
            event != null && userIsReady && isCalibrated -> {
                Log.i("CONDITION CHECKER", "is ready & is calibrated")
                var count = 0
                var sampleX: Float = 0.0f
                var sampleY: Float = 0.0f
                var sampleZ: Float = 0.0f
                do {
                    sampleX += event.values[0]
                    sampleY += event.values[1]
                    sampleZ += event.values[2]
                    count++
                }
                while (count < 65)

                sampleX /= count
                sampleY /= count
                sampleZ /= count

                when { //filter2
                    sampleX <= 3 && sampleX >= -3 -> sampleX = 0.0f


                }
                when {
                    sampleY <= 3 && sampleY >= -3 -> sampleY = 0.0f
                }
                when
                {sampleZ <= 3 && sampleZ >= -3 -> sampleZ = 0.0f}

                accelerationX = sampleX
                accelerationY = sampleY
                accelerationZ = sampleZ
                xAccelerations.add(accelerationX)
                yAccelerations.add(accelerationY)
                zAccelerations.add(accelerationZ)
                accelXText.text = "Acceleration of X: $sampleX\n"
                accelYText.text =  "Acceleration of Y: $sampleY\n"
                accelZText.text = "Acceleration of Z: $sampleZ"

                Log.i("VALUE CHECK", "the size of the xAccelerations array is " + xAccelerations.size+ ". (Speedometer)"  )
                Log.i("VALUE CHECK", "previous sample data is " + xAccelerations[xAccelerations.size - 2]+ "new sample data is" + xAccelerations.lastElement() + ". (Speedometer)"  )
                Log.i("VALUE CHECK", "the size of the yAccelerations array is " + yAccelerations.size+ ". (Speedometer)"  )
                Log.i("VALUE CHECK", "previous sample data is " + yAccelerations[yAccelerations.size - 2]+ "new sample data is" + yAccelerations.lastElement() + ". (Speedometer)"  )
                Log.i("VALUE CHECK", "the size of the xAccelerations array is " + zAccelerations.size+ ". (Speedometer)"  )
                Log.i("VALUE CHECK", "previous sample data is " + zAccelerations[zAccelerations.size - 2]+ "new sample data is" + zAccelerations.lastElement() + ". (Speedometer)"  )

                var x:Array<Float> = arrayOf(xAccelerations[xAccelerations.size - 2],xAccelerations.lastElement())
                var y:Array<Float> = arrayOf(yAccelerations[yAccelerations.size - 2],yAccelerations.lastElement())
                var z:Array<Float> = arrayOf(zAccelerations[zAccelerations.size - 2],zAccelerations.lastElement())
                getSpeed(x,y,z) //time to get speed once all data is filtered
            }
            event != null && !isCalibrated -> {
                Log.i("CONDITION CHECKER", "is not calibrated")
                calibrateDevice(event)
            }
        }

    }
}


