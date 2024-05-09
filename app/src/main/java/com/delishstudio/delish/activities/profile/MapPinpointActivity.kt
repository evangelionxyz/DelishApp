package com.delishstudio.delish.activities.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.delishstudio.delish.R
import com.delishstudio.delish.databinding.ActivityMapPinpointBinding
import com.delishstudio.delish.system.LocationUtils
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class MapPinpointActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnCameraMoveListener {

    private lateinit var mBinding: ActivityMapPinpointBinding
    private lateinit var mMap: GoogleMap
    private lateinit var mCurrentLocation: LatLng
    private lateinit var mLastLocation: LatLng
    private lateinit var mCenterMarker: Marker
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val mPermissionCode: Int = 101

    private val mHandler = Handler(Looper.getMainLooper())
    private val CAMERA_MOVE_DELAY: Long = 340 // milliseconds

    private val updateLocationRunnable = Runnable {
        updateUserLocation()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mBinding = ActivityMapPinpointBinding.inflate(layoutInflater)
        setContentView(mBinding.root)

        fusedLocationProviderClient =  LocationServices.getFusedLocationProviderClient(this)
        fetchLocation()

        mBinding.btBack.setOnClickListener {
            super.onBackPressedDispatcher.onBackPressed()
        }

        // back to previous activity
        mBinding.btSelectLocation.setOnClickListener {
            val intent = Intent().apply {
                putExtra("selectedAddress", mMap.cameraPosition.target)
            }
            setResult(Activity.RESULT_OK, intent)
            finish()
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        var userPos = mLastLocation
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(userPos))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userPos, 17f))
        mCenterMarker = addMarker(LatLng(googleMap.cameraPosition.target.latitude, googleMap.cameraPosition.target.longitude))

        googleMap.setOnCameraMoveListener(this)

        mBinding.btUseCurrentLocation.setOnClickListener {
            mLastLocation = mCurrentLocation
            googleMap.animateCamera(CameraUpdateFactory.newLatLng(userPos), 600, null)
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(userPos, 20f), 1000, null)

            updateUserLocation()
        }
    }

    private fun addMarker(latLng: LatLng): Marker {

        val marker = mMap.addMarker(
            MarkerOptions()
            .position(latLng)
            .draggable(false))

        return marker!!
    }

    private fun updateUserLocation() {
        val camPos = mMap.cameraPosition.target
        mBinding.txtAddress.setText(LocationUtils.getAddress(this, camPos)?.subLocality)
        mBinding.txtSubAddress.setText(LocationUtils.getFullAddress(this, camPos))
    }

    private fun fetchLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), mPermissionCode)
        }

        val task = fusedLocationProviderClient.lastLocation
        task.addOnSuccessListener { location ->
            if (location != null) {
                val latLng = intent.getParcelableExtra<LatLng>("userLatLng")
                if (latLng != null) {
                    mCurrentLocation = latLng
                } else {
                    mCurrentLocation = LatLng(location.latitude, location.longitude)
                }
                mLastLocation = mCurrentLocation
                val mapFragment = supportFragmentManager.findFragmentById(R.id.mapPinpointFragment) as SupportMapFragment
                mapFragment.getMapAsync(this)
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String?>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            mPermissionCode -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchLocation()
                }
            }
        }
    }

    override fun onCameraMove() {
        mCenterMarker.position = mMap.cameraPosition.target

        mHandler.removeCallbacks(updateLocationRunnable)
        mHandler.postDelayed(updateLocationRunnable, CAMERA_MOVE_DELAY)
    }

    override fun onDestroy() {
        super.onDestroy()

        mHandler.removeCallbacks(updateLocationRunnable)
    }
}