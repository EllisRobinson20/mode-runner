package com.college.moderunnermvp

import android.util.Log
import androidx.lifecycle.ViewModel
import java.util.*

class SharedMessage : ViewModel() {
    var SpeedometerFragmentMsg = ""
    var SettingsFragmentMsg = ""
    var SpeedometerFragmentModel: Vector<GpsSample> = Vector(0,1)

    override fun onCleared() {
        super.onCleared()
        Log.d("Trace", "View Model Cleared")
    }

    inner class UIModel{

    }
}