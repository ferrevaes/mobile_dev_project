package com.pxl.parkingApp.Activity

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import androidx.annotation.RequiresApi
import android.icu.util.Calendar
import androidx.appcompat.app.AppCompatActivity
import com.pxl.parkingApp.R
import com.pxl.parkingApp.Utilities.NotificationUtils
import kotlinx.android.synthetic.main.activity_timer.*


class TimerActivity : AppCompatActivity() {
    private val mNotificationTime = java.util.Calendar.getInstance().timeInMillis + 5000 //Set after 5 seconds from the current time.

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timer)

        setTimerBtn.setOnClickListener {
            timeTv.text = Calendar.getInstance().timeInMillis.toString()
            val cal = Calendar.getInstance()
            val timeSetListener = TimePickerDialog.OnTimeSetListener{ timePicker, hour, minute ->
                cal.set(Calendar.HOUR_OF_DAY, hour)
                cal.set(Calendar.MINUTE, minute)
                cal.set(Calendar.SECOND, 0)

                timeTv.text = cal.timeInMillis.toString()
                NotificationUtils().setNotification(cal.timeInMillis, this@TimerActivity)
            }
            TimePickerDialog(this, timeSetListener, cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), true).show();
        }
    }
}
