package com.college.moderunnermvp

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fragment_settings.*

class FragmentSettings : Fragment() {
    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }
    var fragmentState = false
    fun fragmentState():Boolean {
        return fragmentState
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    var distanceInput: EditText? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view: View = inflater.inflate(R.layout.fragment_settings, container, false)
        val distanceInput : EditText = view.findViewById(R.id.distance_input)
            distanceInput.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                   model.SettingsFragmentMsg = s.toString()
                    //Toast.makeText(activity, model.SettingsFragmentMsg, Toast.LENGTH_SHORT).show()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }
            })
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        fragmentState = true
        var editable = Editable.Factory()
        distanceInput?.text = editable.newEditable("100")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onResume() {
        distanceInput = view!!.findViewById(R.id.distance_input)
        super.onResume()
    }
}