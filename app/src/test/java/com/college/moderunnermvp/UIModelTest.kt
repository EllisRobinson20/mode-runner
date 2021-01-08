package com.college.moderunnermvp

/**
 * check for correct average speed readings
 * */

import com.google.android.gms.maps.model.LatLng
import com.google.common.truth.Truth
import org.junit.Test
import java.util.*

class UIModelTest {


    @Test
    fun averageSpeedExp() {
        val sharedMessage = SharedMessage()
        val result = sharedMessage.UIModel().averageSpeed()
        Truth.assertThat(result).isEqualTo(((20.0*21.0)/2.0)/20.0)
    }

    @Test
    fun averageSpeedLinear() {
        val sharedMessage = SharedMessage()
        val result = sharedMessage.UIModel().averageSpeed()
        Truth.assertThat(result).isEqualTo((2.0*20.0)/20.0)
    }
    @Test
    fun instantaneousAverageSpeedExp() { // iterator should be range 2..21
        val sharedMessage = SharedMessage()
        val result = sharedMessage.UIModel().instantaneousAverageSpeed()
        Truth.assertThat(result).isEqualTo(((21.0*23.0)/2.0)/21.0)
    }
    @Test
    fun instantaneousAverageSpeedLinear() {
        val sharedMessage = SharedMessage()
        val result = sharedMessage.UIModel().instantaneousAverageSpeed()
        Truth.assertThat(result).isEqualTo((2.0*20.0)/20.0)
    }
}