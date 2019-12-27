package com.pxl.parkingApp.Fragments.Home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding

import com.pxl.parkingApp.R
import com.pxl.parkingApp.models.Parking
import kotlinx.android.synthetic.main.fragment_parking_detail.view.*

/**
 * A simple [Fragment] subclass.
 */
class ParkingDetailFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = DataBindingUtil.inflate<ViewDataBinding>(inflater, R.layout.fragment_parking_detail, container, false)

        val parking = arguments?.get("parking") as Parking
        binding.root.txtName.text = parking.name
        binding.root.txtLong.text = parking.longitude.toString()
        binding.root.txtLat.text = parking.latitude.toString()
        binding.root.txtAddress.text = parking.address
        binding.root.txtTotalPlaces.text = parking.total_places.toString()
        return binding.root
    }
}
