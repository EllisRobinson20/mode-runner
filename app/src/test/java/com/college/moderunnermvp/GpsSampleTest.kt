package com.college.moderunnermvp

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import org.junit.Test
/**
 * calculate speed with values: distance 10m, time 5s
 * should equal: speed == 2ms
 * calculate acceleration with values: initialTime 0s, finalTime 5s, initialSpeed 0m, finalSpeed 10m
 * should equal: acceleration == 2ms(squared)
 * return isFaster with: topSpeed 3
 * should equal: false
 * return isFaster with: topSpeed 2
 * should equal: false
 * return isFaster with: topSpeed 1
 * should equal: true
 * distance remaining should == difference between targetDistance and totalDistance
 * */

var distanceFrame: Float = 10.0f
var timeFrame: Double = 5.0
var overallTime: Double = 5.0
var topSpeed: Double = 1.0
var target: Int = 100
var totalDistance: Double = 10.0
var serviceIsRunning: Boolean = true
var serviceComplete: Boolean = false
var latLong:LatLng = LatLng(0.0,0.0)
var gpsSample: GpsSample = GpsSample(distanceFrame,timeFrame, overallTime,topSpeed,target,totalDistance,serviceIsRunning, serviceComplete,latLong)
class GpsSampleTest {

    @Test
    fun calculateSpeed() {

        val result = gpsSample.calculateSpeed()
        Truth.assertThat(result).isEqualTo(2.0)
    }
    @Test
    fun calculateAcceleration1() {

        val result = gpsSample.calculateAcceleration()
        Truth.assertThat(result).isEqualTo(0.4)
    }
    @Test
    fun calculateAcceleration2() {
        var gpsSample: GpsSample = GpsSample(distanceFrame,2.0, overallTime,topSpeed,target,totalDistance,serviceIsRunning, serviceComplete,latLong)
        val result = gpsSample.calculateAcceleration()
        Truth.assertThat(result).isEqualTo(2.5)
    }
    @Test
    fun isFaster() {
        val result = gpsSample.isFaster()
        Truth.assertThat(result).isTrue()
    }
    @Test
    fun isFasterThan2() {
        var gpsSample: GpsSample = GpsSample(distanceFrame,timeFrame, overallTime,2.0,target,totalDistance,serviceIsRunning, serviceComplete,latLong)
        val result = gpsSample.isFaster()
        Truth.assertThat(result).isFalse()
    }
    @Test
    fun isFasterThan3() {
        var gpsSample: GpsSample = GpsSample(distanceFrame,timeFrame, overallTime,3.0,target,totalDistance,serviceIsRunning, serviceComplete,latLong)
        val result = gpsSample.isFaster()
        Truth.assertThat(result).isFalse()
    }
    @Test
    fun distanceRemaining() {
        val result = gpsSample.target - gpsSample.totalDistance
        Truth.assertThat(result).isEqualTo(90.0)
    }
}