package com.pxl.parkingApp.Activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.firebase.database.*
import com.pxl.parkingApp.Adapter.ParkingAdapter
import com.pxl.parkingApp.models.Parking
import kotlinx.android.synthetic.main.activity_maps.*
import com.google.android.gms.maps.model.*
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import android.graphics.Bitmap
import android.graphics.Canvas
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.gms.maps.model.BitmapDescriptor
import com.pxl.parkingApp.R



class HomeActivity : AppCompatActivity() {
    private lateinit var mMap: GoogleMap
    private lateinit var mParkingReference: DatabaseReference
    private lateinit var mAdapter: ParkingAdapter
    private lateinit var mParkings: ArrayList<Parking>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val navController = this.findNavController(R.id.navHostFragment)
        NavigationUI.setupActionBarWithNavController(this, navController)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = this.findNavController(R.id.navHostFragment)
        return navController.navigateUp()
    }
}
