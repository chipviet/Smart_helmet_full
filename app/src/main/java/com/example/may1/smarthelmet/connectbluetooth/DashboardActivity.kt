package com.example.may1.smarthelmet.connectbluetooth

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.content.pm.PackageManager
import android.media.MediaPlayer
import android.os.*
import android.support.v7.app.AppCompatActivity
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.DividerItemDecoration
import android.telephony.SmsManager
import android.util.Log
import android.view.View
import android.widget.*
import com.example.may1.smarthelmet.Class.Data
import com.example.may1.smarthelmet.Class.UpdateData
import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.MusicDashboardActivity
import com.example.may1.smarthelmet.R
import com.example.may1.smarthelmet.SettingsDashboardActivity
import com.example.may1.smarthelmet.registerlogin.RegisterActivity
import com.example.may1.smarthelmet.trackingdata.LatestDataTrackingActivity
import com.example.may1.smarthelmet.trackingdata.NewUserTrackingActivity
import com.example.may1.smarthelmet.trackingdata.TrackingActivity
import com.example.may1.smarthelmet.trackingdata.UserItem
import com.example.may1.smarthelmet.viewstrackingdata.LatestDataRow
import com.google.android.gms.location.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.activity_latest_data_tracking.*
import kotlinx.android.synthetic.main.activity_music_dashboard.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_tracking_activity.*
import kotlinx.android.synthetic.main.new_user_tracking_activity.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.text.SimpleDateFormat
import java.util.*

 class DashboardActivity : AppCompatActivity() {
//*******************************************Index tracking*****************************************

//        internal val humidity : String
        internal var humidity = String()
        internal var temperature = String ()
        internal var alcohol_concentration = String ()
        internal var examinate_alcohol = String ()
        internal var examinate_impact  = String ()
        internal var  sleeping_level = String ()


//*******************************************Warning sound******************************************

        internal lateinit var mp: MediaPlayer

        internal lateinit var mp_alcohol: MediaPlayer

        internal lateinit var mp_impact: MediaPlayer

//*******************************************Tracking***********************************************

//        val adapter = GroupAdapter<ViewHolder>()
//
//        companion object {
//             var currentUser: User? = null
//            val TAG = "LatestMessages"
//        }
//
//        var toUser: User? = null

//     Newuseractivity
     companion object {
         val USER_KEY = "USER_KEY"
     }
     val adapter = GroupAdapter<ViewHolder>()

     var toUser: User? = null

//*******************************************Bluetooth**************************************************************
//     internal var btnOn: Button,
//     internal var btnOff:Button
//     internal var txtArduino: TextView? = null,
//     internal var txtString:TextView
//     internal var txtStringLength:TextView
//     internal var sensorView0:TextView
//     internal var sensorView1:TextView
//     internal var sensorView2:TextView
//     internal var sensorView3:TextView
//     internal var get_data:TextView? = null
//*************************************************************
//     internal lateinit var bluetoothIn: Handler
//
//     internal val handlerState = 0                        //used to identify handler message
//     private var btAdapter: BluetoothAdapter? = null
//     private var btSocket: BluetoothSocket? = null
//     private val recDataString = StringBuilder()
//
//     private var mConnectedThread: ConnectedThread? = null

     // SPP UUID service - this should work for most devices
     private val BTMODULEUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
//
//     // String for MAC address
//     private val address: String? = null
//
//     //**************************************************
     internal lateinit var bluetoothIn: Handler

     internal val handlerState = 0                        //used to identify handler message
     private var btAdapter: BluetoothAdapter? = null
     private var btSocket: BluetoothSocket? = null
     private val recDataString = StringBuilder()

     private var mConnectedThread: ConnectedThread? = null
//*****************************************current location************************************************************


    lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    lateinit var locationRequest: LocationRequest
    lateinit var locationCallback: LocationCallback

     internal var currentLatitude = String()
     internal var currentLongtitude = String()

     //lateinit var  sendLocationLatitude  :String
     //     lateinit var  sendLocationLongitude  :String



     //*********************************************************************************************************************
    @SuppressLint("HandlerLeak")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

//*******************************************Warning sound************************************************************
         mp = MediaPlayer.create(this,R.raw.alarm_sound)
         mp.isLooping = true
         mp.setVolume(0.5f, 0.5f)


         mp_alcohol = MediaPlayer.create(this, R.raw.nongdocon_1)
         mp_alcohol.isLooping = true
         mp_alcohol.setVolume(0.5f,0.5f)

         mp_impact = MediaPlayer.create(this, R.raw.vacham)
         mp_impact.isLooping = true
         mp_impact.setVolume(0.5f,0.5f)
//         warning_btn.setOnClickListener {
//             mp?.start()
//         }
//         stop_btn.setOnClickListener {
//             mp.start()
//         }
 //*****************************************Tracking***********************************************
         //Trackingactivity

//         recyclerview_send_data_tracking.adapter = adapter
//         recyclerview_send_data_tracking.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
//
//         adapter.setOnItemClickListener { item, view ->
//             Log.d(LatestDataTrackingActivity.TAG, "123")
//             val intent = Intent(this, DashboardActivity::class.java)
//
//             // we are missing the chat partner user
//
//             val row = item as LatestDataRow
//             intent.putExtra(NewUserTrackingActivity.USER_KEY, row.updatePartnerUser)
//             //user_key.setText("fuck:"+ NewUserTrackingActivity.USER_KEY.toString())
//             startActivity(intent)
//         }
//
//         toUser = intent.getParcelableExtra<User>(NewUserTrackingActivity.USER_KEY)
//

//         verifyUserIsLoggedIn()
//         fetchCurrentUser()

         //NewUseractivity
         //toUser = intent.getParcelableExtra<User>(NewUserTrackingActivity.USER_KEY)


        //*********************************tools************************************************************************
        home_music_activity.setOnClickListener {
            val intent = Intent(this, MusicDashboardActivity::class.java)
            startActivity(intent)
        }
        home_tracking_activity.setOnClickListener {
            val intent = Intent(this, LatestDataTrackingActivity::class.java)
            startActivity(intent)
        }
        home_settings_activity.setOnClickListener {
            val intent = Intent(this, SettingsDashboardActivity::class.java)
            startActivity(intent)
        }

        //Open google maps
        home_map_activity.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
           // val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.grab")
            if (launchIntent != null) {
                startActivity(launchIntent)//null pointer check in case package name was not found
            }
            Log.d("AAA","running")
        }

        //*************************************************************************************************************

        dateAndTime()

        //*************************************************get location**************************************************

        val REQUEST_CODE = 1000;

        //Check permission
        if(ActivityCompat.shouldShowRequestPermissionRationale(this,android.Manifest.permission.ACCESS_FINE_LOCATION))
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
        else {
            buildLocationRequest()
            buildLocationCallBack()

            //Create FusedProviderClient
            fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
            //set event


                if (ActivityCompat.checkSelfPermission(this@DashboardActivity,android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this@DashboardActivity,android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED )
                {
                    ActivityCompat.requestPermissions(this@DashboardActivity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),REQUEST_CODE)
                    return
                }
                fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper())

        }
//*************************************************** bluetooth*********************************************************
        //Link the buttons and textViews to respective views
        bluetoothIn = @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: android.os.Message) {
                    if (msg.what == handlerState) {                                     //if message is what we want
                    val readMessage =
                        msg.obj as String                                                  // msg.arg1 = bytes from connect thread
                    recDataString.append(readMessage)                                      //keep appending to string until ~
                    val endOfLineIndex = recDataString.indexOf("~")                    // determine the end-of-line
                    if (endOfLineIndex > 0) {                                           // make sure there data before ~
                        var dataInPrint = recDataString.substring(0, endOfLineIndex)    // extract string
                      //  txtArduino.setText("Data Received = $dataInPrint")
                        Log.d("Data", "Data Received = $dataInPrint")
                        val dataLength = dataInPrint.length                          //get length of data received
                        //txtArduino.setText("String Length = $dataLength")

                        if (recDataString[0] == '#')
                        //if it starts with # we know it is what we are looking for
                        {
                             humidity = recDataString.substring(9, 14)                //get sensor value from string between indices 1-5
                            temperature = recDataString.substring(15, 20)            //same again...
                             alcohol_concentration = recDataString.substring(21, 25)

                            //check station
                             examinate_alcohol = recDataString.substring(1, 2)
                            sleeping_level = recDataString.substring(3,4)
                            val sound_off = recDataString.substring(5,6)
                            examinate_impact= recDataString.substring(7,8)
                           // val send_sms = recDataString.substring(9,10)

                            sensorView0.text = " Humidity = " + humidity + "%"                  //update the textviews with sensor values
                            sensorView1.text = " Temperature = " + temperature + "°C"
                            sensorView2.text = "  Alcohol content = " + alcohol_concentration + "mg/l"
                            //sensorView3.text = " Sensor 3 Voltage = " + examinate_alcohol + "V"

                           saveDataToFirebaseDatabase()

                            //**************************Handler**********************************************

                            if(examinate_alcohol == "1") {
                                //sendSMSAlcohol()
                                mp_alcohol.start()
                                Log.d("data","Nồng độ vượt mức cho phép")
                                test_sleeping.setText("Nồng độ cồn vượt mức cho phép ");
                            }
                            if(examinate_impact =="1") {
                                //sendSMSImpact()
                                //SleepingWarning()
                                mp_impact.start()
                                Log.d("data","Có va đập")
                                test_sleeping.setText("Có va đập");
                              //  openSound()
                            }
                            if(sleeping_level =="1") {
                                Log.d("data","buồn ngủ")
                                //openSound()
                                test_sleeping.setText("buồn ngủ");
                                mp.start()
                            }
                            if(sound_off == "1") {
                               // pauseSound()
                                if (mp.isPlaying) {
                                    // Stop
                                    mp.pause()
                                    //playBtn.setBackgroundResource(R.drawable.play)

                                }
                                if(mp_alcohol.isPlaying) {
                                    mp_alcohol.pause()
                                }
                                if(mp_impact.isPlaying) {
                                    mp_impact.pause()
                                }
                            }
//                            stop_btn.setOnClickListener {
////                                openSound()
//                                mp.start()
//                               }
//                            warning_btn.setOnClickListener {
//                                //pauseSound()
//                                mp.pause()
//                            }

                        }
                        recDataString.delete(0, recDataString.length)                    //clear all string data
                        // strIncom =" ";
                        dataInPrint = " "
                        //performSendMessage()
                    }
                }
            }
        }

        btAdapter = BluetoothAdapter.getDefaultAdapter()       // get Bluetooth adapter
        checkBTState()

      //   Set up onClick listeners for buttons to send 1 or 0 to turn on/off LED
   //     buttonOff.setOnClickListener(View.OnClickListener {
//            mConnectedThread?.write("a")    // Send "0" via Bluetooth
//            Toast.makeText(baseContext, "Turn off LED", Toast.LENGTH_SHORT).show()
//            Log.d("Data", "Send a character")
//        })
//
//        buttonOn.setOnClickListener {
//            mConnectedThread?.write("b")    // Send "1" via Bluetooth
//            Toast.makeText(baseContext, "Turn on LED", Toast.LENGTH_SHORT).show()
//            Log.d("Data", "Send b character")
//        }

        //**************************************************SendSMS****************************************************

        ActivityCompat.requestPermissions(
            this@DashboardActivity,
            arrayOf(Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS),
            PackageManager.PERMISSION_GRANTED
        )
    //*****************************************************SLeepingWarning*********************************************
//         warning_btn.setOnClickListener {
//             SleepingWarning()
//         }



    }



//*********************************************Bluetooth*************************************************************

@Throws(IOException::class)
private fun createBluetoothSocket(device: BluetoothDevice): BluetoothSocket {

    return device.createRfcommSocketToServiceRecord(BTMODULEUUID)
    //creates secure outgoing connecetion with BT device using UUID
}

     public override fun onResume() {
         super.onResume()

         //Get MAC address from DeviceListActivity via intent
         val intent = intent

         //Get the MAC address from the DeviceListActivty via EXTRA
         //address = intent.getStringExtra(DeviceListActivity.EXTRA_DEVICE_ADDRESS);

         //create device and set the MAC address
         val device = btAdapter!!.getRemoteDevice("98:D3:31:FD:2A:B9")

         try {
             btSocket = createBluetoothSocket(device)
         } catch (e: IOException) {
             Toast.makeText(baseContext, "Socket creation failed", Toast.LENGTH_LONG).show()
         }

         // Establish the Bluetooth socket connection.
         try {
             btSocket!!.connect()
         } catch (e: IOException) {
             try {
                 btSocket!!.close()
             } catch (e2: IOException) {
                 //insert code to deal with this
             }

         }

         mConnectedThread = ConnectedThread(btSocket!!)
         mConnectedThread!!.start()

         //I send a character when resuming.beginning transmission to check device is connected
         //If it is not an exception will be thrown in the write method and finish() will be called
         mConnectedThread!!.write("x")
     }

     public override fun onPause() {
         super.onPause()
         try {
             //Don't leave Bluetooth sockets open when leaving activity
             btSocket!!.close()
         } catch (e2: IOException) {
             //insert code to deal with this
         }

     }

     //Checks that the Android device Bluetooth is available and prompts to be turned on if off
     private fun checkBTState() {

         if (btAdapter == null) {
             Toast.makeText(baseContext, "Device does not support bluetooth", Toast.LENGTH_LONG).show()
         } else {
             if (btAdapter!!.isEnabled()) {
             } else {
                 val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                 startActivityForResult(enableBtIntent, 1)
             }
         }
     }

     //create new class for connect thread
     private inner class ConnectedThread//creation of the connect thread
         (socket: BluetoothSocket) : Thread() {
         private val mmInStream: InputStream?
         private val mmOutStream: OutputStream?

         init {
             var tmpIn: InputStream? = null
             var tmpOut: OutputStream? = null

             try {
                 //Create I/O streams for connection
                 tmpIn = socket.inputStream
                 tmpOut = socket.outputStream
             } catch (e: IOException) {
             }

             mmInStream = tmpIn
             mmOutStream = tmpOut
         }

         override fun run() {
             val buffer = ByteArray(1024)
             var bytes: Int

             // Keep looping to listen for received messages
             while (true) {
                 try {
                     bytes = mmInStream!!.read(buffer)            //read bytes from input buffer
                     val readMessage = String(buffer, 0, bytes)
                     // Send the obtained bytes to the UI Activity via handler
                     bluetoothIn.obtainMessage(handlerState, bytes, -1, readMessage).sendToTarget()
                     Log.d("Data", "readMessage:$readMessage")
                     //get_data.setText(readMessage.toString());

                 } catch (e: IOException) {
                     break
                 }

             }
         }

         //write method
         fun write(input: String) {
             val msgBuffer = input.toByteArray()           //converts entered String into bytes
             try {
                 mmOutStream!!.write(msgBuffer)                //write bytes over BT connection via outstream
             } catch (e: IOException) {
                 //if you cannot write, close the application
                 Toast.makeText(baseContext, "Connection Failure", Toast.LENGTH_LONG).show()
                 finish()

             }

         }
     }
//**************************************************Date and Time***********************************************************
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
    //*****************************************current location****************************************************

    private fun buildLocationCallBack() {
        locationCallback = object : LocationCallback() {
            @SuppressLint("SetTextI18n")
            override fun onLocationResult(p0: LocationResult?) {

                var location = p0!!.locations.get(p0!!.locations.size-1)
                txt_location.text = location.latitude.toString()+ "/"+location.longitude.toString()
                currentLatitude = location.latitude.toString()
                currentLongtitude = location.longitude.toString()

            }
        }
    }

    private fun buildLocationRequest() {
        locationRequest = LocationRequest()
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = 5000
        locationRequest.smallestDisplacement = 10f
    }
    //*********************************************send SMS******************************************************

     private fun sendSMS() {

         val userID = FirebaseAuth.getInstance().uid
         val ref = FirebaseDatabase.getInstance().getReference("/users/$userID")
         ref.addListenerForSingleValueEvent(object: ValueEventListener {
             override fun onDataChange(p0: DataSnapshot) {
                 val user = p0.getValue(User::class.java)
                 val number = "0329456195"
                 val name = user?.username

                 val message =name.toString()+"Location:"+ currentLatitude.toString() +currentLongtitude.toString() +"\n" + "Alcohol Content:" +   alcohol_concentration.toString() +"mg/L \n" + "Heart beat: 0 bpm "
                 //val number = editTextNumber.text.toString()

                 val mySmsManager = SmsManager.getDefault()
                 mySmsManager.sendTextMessage(number, null, message, null, null)
             }

             override fun onCancelled(p0: DatabaseError) {

             }
         })

     }



     // //Send SMS
     //    private fun sendSMS() {
     //
     //      //  val number: String
     //        val UserID = FirebaseAuth.getInstance().uid
     //        val ref = FirebaseDatabase.getInstance().getReference("/users/$UserID")
     //        ref.addListenerForSingleValueEvent(object : ValueEventListener {
     //            override fun onDataChange(p0: DataSnapshot) {
     //                val user =p0.getValue(User::class.java)
     //                 // = user?.phone_1
     //                Log.d("DashboardActivity","UserID:$UserID")
     //
     //                val message = sendLocationLatitude + "," + sendLocationLongitude
     //                //val number = editTextNumber.text.toString()
     //                val number = user?.phone_1
     //
     //                val mySmsManager = SmsManager.getDefault()
     //                mySmsManager.sendTextMessage(number, null, message, null, null)
     //            }
     //
     //            override fun onCancelled(p0: DatabaseError) {
     //
     //            }
     //        })
     //
     //
     ////        val message = "cccc"+ editText.getText().toString()
     ////        //val number = editTextNumber.text.toString()
     ////
     ////        val mySmsManager = SmsManager.getDefault()
     ////        mySmsManager.sendTextMessage(number, null, message, null, null)
     //    }


//*********************************************Tracking*********************************************
//    private fun performSendMessage() {
//        // how do we actually send a message to firebase...
//        val text = "Location:"+ currentLatitude.toString() +currentLongtitude.toString() +"\n" + "Alcohol Content:" +   alcohol_concentration.toString() +"mg/L \n" + "Heart beat: 0 bpm "
//
//        val fromId = FirebaseAuth.getInstance().uid
//        val user = intent.getParcelableExtra<User>(NewUserTrackingActivity.USER_KEY)
//        val toId = user.uid
//
//        if (fromId == null) return
//
////    val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
//        val reference = FirebaseDatabase.getInstance().getReference("/user-messages/$fromId/$toId").push()
//
//        val toReference = FirebaseDatabase.getInstance().getReference("/user-messages/$toId/$fromId").push()
//
//        val chatMessage = UpdateData(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
//
//        reference.setValue(chatMessage)
//            .addOnSuccessListener {
//                Log.d(TrackingActivity.TAG, "Saved our chat message: ${reference.key}")
//                //edittext_chat_log.text.clear()
//                recyclerview_chat_log.scrollToPosition(adapter.itemCount - 1)
//            }
//
//        toReference.setValue(chatMessage)
//
//        val latestMessageRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId/$toId")
//        latestMessageRef.setValue(chatMessage)
//
//        val latestMessageToRef = FirebaseDatabase.getInstance().getReference("/latest-messages/$toId/$fromId")
//        latestMessageToRef.setValue(chatMessage)
//    }
//
//     private fun fetchCurrentUser() {
//         val uid = FirebaseAuth.getInstance().uid
//         val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
//         ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//             override fun onDataChange(p0: DataSnapshot) {
//                 LatestDataTrackingActivity.currentUser = p0.getValue(User::class.java)
//                 // Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
//             }
//
//             override fun onCancelled(p0: DatabaseError) {
//
//             }
//         })
//     }
////     private fun verifyUserIsLoggedIn() {
////         val uid = FirebaseAuth.getInstance().uid
////         if (uid == null) {
////             val intent = Intent(this, RegisterActivity::class.java)
////             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
////             startActivity(intent)
////         }
////     }
//
//
//
//
////************************************************Tracking************************************
//private fun fetchUsers() {
//    val ref = FirebaseDatabase.getInstance().getReference("/users")
//    ref.addListenerForSingleValueEvent(object: ValueEventListener {
//
//        override fun onDataChange(p0: DataSnapshot) {
//
//            val adapter = GroupAdapter<ViewHolder>()
//
//            p0.children.forEach {
//                Log.d("NewMessage", it.toString())
//                val user = it.getValue(User::class.java)
//                if (user != null) {
//                    adapter.add(UserItem(user))
//                }
//            }
//
//            adapter.setOnItemClickListener { item, view ->
//
//                val userItem = item as UserItem
//
//                val intent = Intent(view.context, DashboardActivity::class.java)
//
//                //Something wrong in here
//                intent.putExtra(NewUserTrackingActivity.USER_KEY,  userItem.user)
////                    intent.putExtra(USER_KEY, userItem.user)
////                    intent.putExtra(USER_KEY,userItem.user)
//                startActivity(intent)
//
//                finish()
//
//            }
//
//            recyclerview_send_data_tracking.adapter = adapter
//        }
//
//        override fun onCancelled(p0: DatabaseError) {
//
//        }
//    })
//}
//
private fun saveDataToFirebaseDatabase() {

    val uid = FirebaseAuth.getInstance().uid ?: ""
    Log.d("RegisterActivity","fuck u :$uid")

    // database = FirebaseDatabase.getInstance().getReference("/users/$uid")
    val ref = FirebaseDatabase.getInstance().getReference("/datas/$uid")
    Log.d("RegisterActivity","i want you action :$ref")
    val data = Data( "Location:"+ currentLatitude.toString() +currentLongtitude.toString() +"\n" + "Alcohol Content:" +   alcohol_concentration.toString() +"mg/L \n" + "Heart beat: 0 bpm ");

    ref.setValue(data)

        .addOnSuccessListener {
            Log.d("RegisterActivity", "Success save data to Firebase")

            //open ConnectViaBluettooth when you success save data to database ***********
        }
        .addOnFailureListener {
            Log.d("RegisterActivity", "Failed to set value to database :${it.message}")
        }

}

 }

