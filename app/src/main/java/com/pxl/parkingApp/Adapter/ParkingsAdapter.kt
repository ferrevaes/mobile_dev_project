package com.pxl.parkingApp.Adapter

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.pxl.parkingApp.R
import com.pxl.parkingApp.models.Parking
import kotlinx.android.synthetic.main.parking_list_item.view.*
import org.w3c.dom.Comment


//TODO: Remove all comments
class ParkingAdapter(private val mContext: Context, private val mDatabaseReference: DatabaseReference, private val onParkingListener: OnParkingListener)
    : RecyclerView.Adapter<ParkingViewHolder>() {
    private val mChildEventListener: ChildEventListener?

    private val mParkingIds = ArrayList<String>()
    private val mParkings = ArrayList<Parking>()

    init {
        // Create child event listener
        // [START child_event_listener_recycler]
        val childEventListener = object : ChildEventListener {
            override fun onChildAdded(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildAdded:" + dataSnapshot.key)
                // A new comment has been added, add it to the displayed list
                val parking = dataSnapshot.getValue<Parking>(Parking::class.java)

                // [START_EXCLUDE]
                // Update RecyclerView
                mParkingIds.add(dataSnapshot.key!!)
                mParkings.add(parking!!)
                notifyItemInserted(mParkings.size - 1)
                // [END_EXCLUDE]
            }

            override fun onChildChanged(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildChanged:" + dataSnapshot.key)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so displayed the changed comment.
                val newParking = dataSnapshot.getValue<Parking>(Parking::class.java)
                val parkingKey = dataSnapshot.key

                // [START_EXCLUDE]
                val parkingIndex = mParkingIds.indexOf(parkingKey)
                if (parkingIndex > -1) {
                    // Replace with the new data
                    mParkings[parkingIndex] = newParking!!

                    // Update the RecyclerView
                    notifyItemChanged(parkingIndex)
                } else {
                    Log.w(TAG, "onChildChanged:unknown_child:$parkingKey")
                }
                // [END_EXCLUDE]
            }

            override fun onChildRemoved(dataSnapshot: DataSnapshot) {
                Log.d(TAG, "onChildRemoved:" + dataSnapshot.key)

                // A comment has changed, use the key to determine if we are displaying this
                // comment and if so remove it.
                val parkingKey = dataSnapshot.key

                // [START_EXCLUDE]
                val parkingIndex = mParkingIds.indexOf(parkingKey)
                if (parkingIndex > -1) {
                    // Remove data from the list
                    mParkingIds.removeAt(parkingIndex)
                    mParkings.removeAt(parkingIndex)

                    // Update the RecyclerView
                    notifyItemRemoved(parkingIndex)
                } else {
                    Log.w(TAG, "onChildRemoved:unknown_child:$parkingKey")
                }
                // [END_EXCLUDE]
            }

            override fun onChildMoved(dataSnapshot: DataSnapshot, previousChildName: String?) {
                Log.d(TAG, "onChildMoved:" + dataSnapshot.key)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w(TAG, "postParkings:onCancelled", databaseError.toException())
                Toast.makeText(mContext, "Failed to load parkings.", Toast.LENGTH_SHORT).show()

            }
        }
        mDatabaseReference.addChildEventListener(childEventListener)
        // [END child_event_listener_recycler]

        // Store reference to listener so it can be removed on app stop
        mChildEventListener = childEventListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ParkingViewHolder(
        LayoutInflater.from(mContext).inflate(R.layout.parking_list_item, parent, false), onParkingListener)

    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        val parking = mParkings[position]
        holder.parkingName.text = parking.name
        holder.parkingAvailablePlaces.text = parking.available_places.toString()
        holder.parkingAddress.text = parking.address
        if(parking.available_places > 0){
            holder.parkingMarker.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_green_foreground))
        }else{
            holder.parkingMarker.setImageDrawable(ContextCompat.getDrawable(mContext, R.drawable.ic_marker_red_foreground))
        }
    }

    override fun getItemCount() = mParkings.size

    fun cleanupListener() {
        if (mChildEventListener != null) {
            mDatabaseReference.removeEventListener(mChildEventListener)
        }
    }

    interface OnParkingListener {
        fun onParkingClick(position: Int)
    }
}