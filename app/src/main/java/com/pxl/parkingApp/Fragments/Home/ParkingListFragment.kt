package com.pxl.parkingApp.Fragments.Home


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentManager
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.firebase.database.*
import com.pxl.parkingApp.Adapter.ParkingAdapter

import com.pxl.parkingApp.R
import com.pxl.parkingApp.models.Parking
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.fragment_parking_list.view.*

class ParkingListFragment : Fragment(), OnMapReadyCallback, ParkingAdapter.OnParkingListener {
    private lateinit var mMap: GoogleMap
    private lateinit var mParkingReference: DatabaseReference
    private lateinit var mAdapter: ParkingAdapter
    private lateinit var mParkings: ArrayList<Parking>
    private lateinit var binding: ViewDataBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_parking_list, container, false)
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mParkings = arrayListOf()
        mParkingReference = FirebaseDatabase.getInstance().getReference("parkings")


        val layoutManager = LinearLayoutManager(activity!!.applicationContext)
        layoutManager.orientation = LinearLayoutManager.VERTICAL
        binding.root.recycler_parkings.layoutManager = layoutManager

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAdapter = ParkingAdapter(activity!!.applicationContext, mParkingReference, this)
        binding.root.recycler_parkings.adapter = mAdapter
    }

    override fun onStop() {
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

            mParkings.add(parking)
        }
    }

    private fun drawMarker(point: LatLng, icon: Boolean){
        val options = MarkerOptions()
        options.position(point)

        if(icon){
            options.icon(bitmapDescriptorFromVector(activity!!.applicationContext, R.drawable.ic_marker_green_foreground))
        }else{
            options.icon(bitmapDescriptorFromVector(activity!!.applicationContext, R.drawable.ic_marker_red_foreground))
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
        val bundle = Bundle()
        bundle.putSerializable("parking", parking)
        Navigation.findNavController(view!!).navigate(R.id.action_parkingListFragment_to_parkingDetailFragment, bundle)
    }
}
