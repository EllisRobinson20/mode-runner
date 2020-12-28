package com.college.moderunnermvp

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [FragmentMap.newInstance] factory method to
 * create an instance of this fragment.
 */
class FragmentMap : Fragment(), OnMapReadyCallback {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var gMap: GoogleMap? = null
    var dTag: String = "FragmentMap"

    private val model: SharedMessage by lazy {
        ViewModelProviders.of(activity as FragmentActivity).get(SharedMessage::class.java)
    }

    //need a list of the latitude and longitude of the gpsObjects
    var polyline: Polyline? = null
    var latLongList = arrayListOf<LatLng>()
    var markerList = arrayListOf<Marker>()

    override fun onCreate(savedInstanceState: Bundle?) {
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
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onResume() {
        var supportMapFragment = childFragmentManager.findFragmentById(R.id.google_map) as SupportMapFragment
        supportMapFragment.getMapAsync(this)

        super.onResume()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment FragmentMap.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FragmentMap().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(p0: GoogleMap?) {

        if (p0 != null) {
            gMap = p0
            gMap!!.setOnMapLoadedCallback(GoogleMap.OnMapLoadedCallback() {
                //for each gps object
                for (gpsObject in model.SpeedometerFragmentModel) {
                    Log.d(dTag, gpsObject.latLong.toString())
                    var latLong:LatLng = gpsObject.latLong!!
                    var markerOptions = MarkerOptions().position(latLong)
                    var marker = gMap!!.addMarker(markerOptions)
                    if (markerList.size > 0)
                        marker.isVisible = false
                    latLongList.add(latLong)
                    markerList.add(marker)


                }

                markerList.last().isVisible = true
                if (polyline!= null) polyline?.remove()
                var polylineOptions = PolylineOptions().addAll(latLongList).clickable(true)
                polyline = gMap!!.addPolyline(polylineOptions)
                polyline!!.width = 8f
                var colorAccent = this.context?.let { ContextCompat.getColor(it, R.color.colorAccent) }
                if (colorAccent != null) {
                    polyline!!.color = colorAccent
                }
                gMap!!.setMinZoomPreference(12f)
                gMap!!.moveCamera(CameraUpdateFactory.newLatLng(latLongList[latLongList.size/2]))
                //gMap!!.setLatLngBoundsForCameraTarget(LatLngBounds(latLongList[0], latLongList.last()))
            })
        }


    }
}