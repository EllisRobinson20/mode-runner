package com.college.moderunnermvp

import android.util.Log
import androidx.lifecycle.ViewModel

class SharedMessage : ViewModel() {
    var SpeedometerFragmentMsg = ""
    var SettingsFragmentMsg = ""

    override fun onCleared() {
        super.onCleared()
        Log.d("Trace", "View Model Cleared")
    }
}