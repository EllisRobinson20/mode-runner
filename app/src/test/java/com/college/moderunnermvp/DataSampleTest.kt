package com.college.moderunnermvp

import com.google.common.truth.Truth
import org.junit.Test

import org.junit.Assert.*

/**
 * Test cases for DataSample
 * For time, Nanos only output should be: 00:00:00:000000010
 * For time, Seconds should be: 00:00:10:000000000
 * For time, Minutes should be: 00:10:00:000000000
 * For time, Hours should be: 10:00:00:000000000
 * When topSpeed == noData: topSpeedNumber should be 0.0
 * When averageSpeed == noData: averageSpeedNumber should be 0.0
 * When peakAcceleration == noData: pealAccelerationNumber should be 0.0
 * */
var dataSample: DataSample = DataSample("val date:String", "100", ".10", "No Record", "No Record", "No Record", "100.00")

class DataSampleTest {

    @Test
    fun initTimeNanos() {
        var dataSample: DataSample = DataSample("val date:String", "100", "0.10", "2.0", "1.0", "0.4", "100.00")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("00:00:00.000000010")
    }
    @Test
    fun initTimeSec() {
        var dataSample: DataSample = DataSample("val date:String", "100", "10.0", "No Record", "No Record", "No Record", "100.0")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("00:00:10")
    }
    @Test
    fun initTimeSec2() {
        var dataSample: DataSample = DataSample("val date:String", "100", "10.01", "No Record", "No Record", "No Record", "100.0")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("00:00:10.000000001")
    }
    @Test
    fun initTimeMin() {
        var dataSample: DataSample = DataSample("val date:String", "100", "0.10.0.0", "No Record", "No Record", "No Record", "100.0")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("00:10")
    }
    @Test
    fun initTimeMin2() {
        var dataSample: DataSample = DataSample("val date:String", "100", "0.10.10.10", "No Record", "No Record", "No Record", "100.0")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("00:10:10.000000010")
    }
    @Test
    fun initTimeHour() {
        var dataSample: DataSample = DataSample("val date:String", "100", "10.0.0.0", "No Record", "No Record", "No Record", "100.0")
        val result = dataSample.initTime()
        Truth.assertThat(result).contains("10:00")
    }
    @Test
    fun noDataTopSpeed() {
        val result = dataSample.topSpeed
        Truth.assertThat(result).contains("No Record")
        val intResult = dataSample.topSpeedNumber
        Truth.assertThat(intResult).isEqualTo(0.0)
    }
    @Test
    fun noDataAvgSpeed() {
        val result = dataSample.averageSpeed
        Truth.assertThat(result).contains("No Record")
        val intResult = dataSample.averageSpeedNumber
        Truth.assertThat(intResult).isEqualTo(0.0)
    }
    @Test
    fun noDataPeakAcceleration() {
        val result = dataSample.peakAcceleration
        Truth.assertThat(result).contains("No Record")
        val intResult = dataSample.peakAccelerationNumber
        Truth.assertThat(intResult).isEqualTo(0.0)
    }
}