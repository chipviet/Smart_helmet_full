package com.example.may1.smarthelmet

import android.annotation.SuppressLint
import android.content.Intent
import android.media.MediaPlayer
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import android.widget.*
import com.example.may1.smarthelmet.connectbluetooth.DashboardActivity
import com.example.may1.smarthelmet.trackingdata.LatestDataTrackingActivity
import kotlinx.android.synthetic.main.activity_music_dashboard.*
import java.text.SimpleDateFormat

class MusicDashboardActivity : AppCompatActivity() {


    //media player
    internal lateinit var mp: MediaPlayer
    private var totalTime: Int = 0

    //list songs
    internal lateinit var listsong: ListView
    internal lateinit var list: MutableList<String>
    internal lateinit var adapter: ListAdapter
    private  var sing: Int =0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_dashboard)

        //************************tools*********************************************************************************
        // music activty -> dashboard activity
        music_home_activity.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }
        // music activity -> call activity
        music_tracking_activity.setOnClickListener {
            val intent = Intent(this,LatestDataTrackingActivity::class.java)
            startActivity(intent)
        }
        //music acitivty -> settings activity
        music_settings_activity.setOnClickListener {
            val intent = Intent(this, SettingsDashboardActivity::class.java)
            startActivity(intent)
        }

        music_map_activity.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
            if (launchIntent != null) {
                startActivity(launchIntent)//null pointer check in case package name was not found
            }
            Log.d("AAA","running")
        }

        //lack of map because i don't know use map activity

        //***********************************************************************************************************
        listsong = findViewById<View>(R.id.list_songs) as ListView
        list = ArrayList()

        val fields = R.raw::class.java.getFields()
        for (i in fields.indices) {
            list.add(fields[i].getName())
        }
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list) as ListAdapter

        list_songs.adapter = adapter
        list_songs.onItemClickListener = AdapterView.OnItemClickListener { adapterView, view, i, l ->
            //             if(mp != null) { mp!!.release() }

            sing = resources.getIdentifier(list[i], "raw", packageName)
            Log.d("TEST", "song:$sing")


            val restID: Int
//            if (sing != null) {
//                restID = 2131427329
//            } else {
            restID = sing
            //}
            mp = MediaPlayer.create(this, restID)
            mp.isLooping = true
            mp.setVolume(0.5f, 0.5f)
            totalTime = mp.duration

            // Volume Bar
            volumeBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekbar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            var volumeNum = progress / 100.0f
                            mp.setVolume(volumeNum, volumeNum)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                }
            )

            // Position Bar
            positionBar.max = totalTime
            positionBar.setOnSeekBarChangeListener(
                object : SeekBar.OnSeekBarChangeListener {
                    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                        if (fromUser) {
                            mp.seekTo(progress)
                        }
                    }

                    override fun onStartTrackingTouch(p0: SeekBar?) {
                    }

                    override fun onStopTrackingTouch(p0: SeekBar?) {
                    }
                }
            )

            // Thread
            Thread(Runnable {
                while (mp != null) {
                    try {
                        var msg = Message()
                        msg.what = mp.currentPosition
                        handler.sendMessage(msg)
                        Thread.sleep(1000)
                    } catch (e: InterruptedException) {
                    }
                }
            }).start()
        }
        //**************************************************************************************************************

        dateAndTime()

        //**************************************************************************************************************
    }
    fun createTimeLabel(time: Int): String {
        var timeLabel = ""
        var min = time / 1000 / 60
        var sec = time / 1000 % 60

        timeLabel = "$min:"
        if (sec < 10) timeLabel += "0"
        timeLabel += sec

        return timeLabel
    }
    fun playBtnClick(v: View) {

        if (mp.isPlaying) {
            // Stop
            mp.pause()
            playBtn.setBackgroundResource(R.drawable.play)

        } else {
            // Start
            mp.start()
            playBtn.setBackgroundResource(R.drawable.stop)
        }
    }

    @SuppressLint("HandlerLeak")
    var handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            var currentPosition = msg.what

            // Update positionBar
            positionBar.progress = currentPosition

            // Update Labels
            var elapsedTime = createTimeLabel(currentPosition)
            elapsedTimeLabel.text = elapsedTime

            var remainingTime = createTimeLabel(totalTime - currentPosition)
            remainingTimeLabel.text = "-$remainingTime"
        }
    }

//    constructor(parcel: Parcel) : this() {
//       totalTime = parcel.readInt()
//   }



    companion object CREATOR : Parcelable.Creator<MusicDashboardActivity> {
        override fun createFromParcel(parcel: Parcel): MusicDashboardActivity {
            return createFromParcel(parcel)
        }

        override fun newArray(size: Int): Array<MusicDashboardActivity?> {
            return arrayOfNulls(size)
        }
    }


    //******************************************************************************************************************
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
    //******************************************************************************************************************

}
