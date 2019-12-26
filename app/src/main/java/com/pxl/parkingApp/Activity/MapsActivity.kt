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
import com.google.android.gms.maps.model.BitmapDescriptor
import com.pxl.parkingApp.R
import android.widget.LinearLayout
import android.widget.Toast

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, ParkingAdapter.OnParkingListener {
    private lateinit var mMap: GoogleMap
    private lateinit var mParkingReference: DatabaseReference
    private lateinit var mAdapter: ParkingAdapter
    private lateinit var mParkings: ArrayList<Parking>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mParkings = arrayListOf()
        mParkingReference = FirebaseDatabase.getInstance().getReference("parkings")


        val layoutManager = LinearLayoutManager(this)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        recycler_parkings.layoutManager = layoutManager
    }

    public override fun onStart() {
        super.onStart()
        mAdapter = ParkingAdapter(this, mParkingReference, this)
        recycler_parkings.adapter = mAdapter
    }

    public override fun onStop() {
        super.onStop()
        mAdapter.cleanupListener()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        loadParkings()

        val hasselt = LatLng(50.930691, 5.332480)
        val cameraPosition = CameraPosition.Builder()
            .target(hasselt)
            .zoom(13f)
            .build()

        mMap.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun loadParkings(){
        mParkingReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                fetchData(dataSnapshot)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                throw databaseError.toException()
            }
        })
    }

    fun fetchData(dataSnapshot: DataSnapshot){
        for (parkingSnapshot in dataSnapshot.children) {
            val parking = parkingSnapshot.getValue(Parking::class.java)

            val icon: Boolean = parking!!.available_places > 0

            drawMarker(LatLng(parking.latitude, parking.longitude), icon)

            mParkings.add(parking);
        }
    }

    private fun drawMarker(point: LatLng, icon: Boolean){
        val options = MarkerOptions()
        options.position(point)

        if(icon){
            options.icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker_green_foreground))
        }else{
            options.icon(bitmapDescriptorFromVector(this, R.drawable.ic_marker_red_foreground))
        }

        mMap.addMarker(options)
    }

    private fun bitmapDescriptorFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        return ContextCompat.getDrawable(context, vectorResId)?.run {
            setBounds(0, 0, intrinsicWidth, intrinsicHeight)
            val bitmap = Bitmap.createBitmap(intrinsicWidth, intrinsicHeight, Bitmap.Config.ARGB_8888)
            draw(Canvas(bitmap))
            BitmapDescriptorFactory.fromBitmap(bitmap)
        }
    }

    override fun onParkingClick(position: Int) {
        val parking = mParkings.get(position)
        Toast.makeText(this, "You have clicked item ${parking.name}", Toast.LENGTH_SHORT).show()
    }
}
