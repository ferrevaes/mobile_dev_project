package com.pxl.parkingApp.Adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.parking_list_item.view.*
import org.w3c.dom.Text

class ParkingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var parkingName: TextView = itemView.tv_parking_name
    var parkingMarker: ImageView = itemView.iv_marker
    var parkingAvailablePlaces: TextView = itemView.tv_parking_available_places
    var parkingAddress: TextView = itemView.tv_parking_address
}