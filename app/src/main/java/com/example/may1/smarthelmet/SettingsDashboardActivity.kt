package com.example.may1.smarthelmet

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import com.example.may1.smarthelmet.connectbluetooth.DashboardActivity
import com.example.may1.smarthelmet.trackingdata.LatestDataTrackingActivity
import com.example.may1.smarthelmet.trackingdata.NewUserTrackingActivity
import kotlinx.android.synthetic.main.activity_settings_dashboard.*
import java.text.SimpleDateFormat

class SettingsDashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings_dashboard)

//*****************************************************************
        settings_music_activity.setOnClickListener {
            val intent = Intent(this, MusicDashboardActivity::class.java)
            startActivity(intent)
        }
        settings_feed_activity.setOnClickListener {
            val intent = Intent(this, LatestDataTrackingActivity::class.java)
            startActivity(intent)
        }
        settings_home_activity.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
        settings_map_activity.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
            if (launchIntent != null) {
                startActivity(launchIntent)//null pointer check in case package name was not found
            }
            Log.d("AAA","running")
        }

//**********************************************************************************************************************

        dateAndTime()

//**********************************************************************************************************************

    }
//**********************************************************************************************************************
    private fun dateAndTime() {
        val t = object : Thread() {
            override fun run() {
                try {
                    while (!isInterrupted) {
                        sleep(1000)
                        runOnUiThread {
                            val tdate = findViewById(R.id.date_textView) as TextView
                            val t_time = findViewById(R.id.time_textView) as TextView
                            val t_standard = findViewById(R.id.standard_textView) as TextView

                            val time = System.currentTimeMillis()
                            val date = System.currentTimeMillis()
                            val standard = System.currentTimeMillis()

                            val sdf = SimpleDateFormat("MMM/dd/yyyy")
                            val sdf_time = SimpleDateFormat("hh:mm:ss")
                            val sdf_standard = SimpleDateFormat("a")

                            val dateString = sdf.format(date)
                            val timeString = sdf_time.format((time))
                            val standardString = sdf_standard.format((standard))

                            tdate.setText(dateString)
                            t_time.setText(timeString)
                            t_standard.setText((standardString))
                        }
                    }
                } catch (e: InterruptedException) {
                }

            }
        }
        t.start()
    }
//**********************************************************************************************************************
}
