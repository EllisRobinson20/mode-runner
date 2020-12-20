package com.college.moderunnermvp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.activity_speedometer.*
import kotlinx.android.synthetic.main.activity_speedometer.tab_layout
import kotlinx.android.synthetic.main.fragment_summary.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentSummary.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentSummary : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var dTag: String = "FragmentSummary"


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d(dTag, "onCreate fired")
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_summary, container, false)
    }

    override fun onResume()
    {
        Log.d(dTag, "onResume fired")
        val adapter = SummaryAdapter(this.childFragmentManager)
        var view_pager = activity!!.findViewById<ViewPager>(R.id.summary_view_pager)
        view_pager?.adapter = adapter
        view_pager.currentItem = 1

        summary_tab_layout.setupWithViewPager(summary_view_pager)
        super.onResume()
    }
    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentSummary.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentSummary().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}