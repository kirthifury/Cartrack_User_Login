package com.cartrack.mobile.codingchallenge.user.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cartrack.mobile.codingchallenge.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    companion object {
        const val LAT_VAL = "lat_value"
        const val LONG_VAL = "long_value"
    }

    private lateinit var mMap: GoogleMap
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun fetchMarkers() {
        if (intent.hasExtra(LAT_VAL) && intent.hasExtra(LONG_VAL)) {
            addMarker(
                LatLng(intent.getDoubleExtra(LAT_VAL, 0.0), intent.getDoubleExtra(LONG_VAL,
                0.0
            ))
            )
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        fetchMarkers()
    }

    private fun addMarker(
        latlng: LatLng
    ) {
        latlng.let {
            mMap.addMarker(MarkerOptions().position(it))
            mMap.moveCamera(CameraUpdateFactory.newLatLng(it))
        }
    }

}