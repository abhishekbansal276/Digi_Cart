package com.example.project_one

import android.Manifest
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.example.project_one.databinding.ActivityHomeScreenBinding
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.Marker
import com.google.firebase.auth.FirebaseAuth

class HomeScreenActivity : AppCompatActivity(), OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityHomeScreenBinding
    lateinit var progressDialog: ProgressDialog

    lateinit var mFusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    val networkChangeListener = NetworlChangeListener()
    private lateinit var mAuth: FirebaseAuth
    lateinit var builder: AlertDialog.Builder

    lateinit var myDialog: Dialog

    companion object{
        private const val LOCATION_REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Loading ...")
        progressDialog.setCancelable(false)

        mAuth = FirebaseAuth.getInstance()
        builder = AlertDialog.Builder(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        binding = ActivityHomeScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        myDialog = Dialog(this)
        myDialog.setContentView(R.layout.image_view)
        myDialog.setCancelable(true)
        myDialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        binding.showStoreMap.setOnClickListener(View.OnClickListener {
            myDialog.show()
        })
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
        mMap.uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerClickListener(this)
        getLastLocation()
    }

    private fun getLastLocation() {
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_REQUEST_CODE)
        }
        mMap.isMyLocationEnabled = true
        mFusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            if(location!=null){
                lastLocation = location
                val curLatLong = LatLng(location.latitude, location.longitude)
                placeMarker(curLatLong)
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLong, 12f))
            }
        }
    }

    private fun placeMarker(curLatLong: LatLng) {
        val markerOptions = MarkerOptions().position(curLatLong)
        markerOptions.title("$curLatLong")
        mMap.addMarker(markerOptions)
    }

    override fun onStart() {
        val filter = IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION)
        registerReceiver(networkChangeListener, filter)
        super.onStart()
    }

    override fun onStop() {
        unregisterReceiver(networkChangeListener)
        super.onStop()
    }

    override fun onMarkerClick(p0: Marker) = false
}