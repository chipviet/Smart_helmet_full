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



//**********************************************************************************************************************

    }
//**********************************************************************************************************************
//**********************************************************************************************************************
}
