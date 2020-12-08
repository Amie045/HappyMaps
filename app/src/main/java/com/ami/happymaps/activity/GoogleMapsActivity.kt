package com.ami.happymaps.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.ami.happymaps.R
import com.ami.happymaps.model.HappyMapsModel

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.android.synthetic.main.activity_google_maps.*


class GoogleMapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap

    private var mHappyMapDetail: HappyMapsModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_google_maps)

        mHappyMapDetail = intent.getParcelableExtra(MainActivity.EXTRA_PLACE_DETAILS)
        mHappyMapDetail.let {
            if (it != null) {
                setSupportActionBar(toolbar)
                supportActionBar?.setDisplayHomeAsUpEnabled (true)
                supportActionBar?.title = it?.title
                toolbar.setNavigationOnClickListener {
                    onBackPressed()
                }
                val supportMapFragment: SupportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
                supportMapFragment.getMapAsync(this)
            }
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
//        val sydney = LatLng(-34.0, 151.0)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        val position = LatLng(mHappyMapDetail!!.latitude, mHappyMapDetail!!.longitude)
        googleMap!!.addMarker(MarkerOptions().position(position).title(mHappyMapDetail!!.location))
        val newLatLngZoom = CameraUpdateFactory.newLatLngZoom(position, 20f)
        googleMap.animateCamera(newLatLngZoom)
    }
}