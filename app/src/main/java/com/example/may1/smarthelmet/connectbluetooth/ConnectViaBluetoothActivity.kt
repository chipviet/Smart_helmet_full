package com.example.may1.smarthelmet.connectbluetooth

import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import android.widget.AdapterView.OnItemClickListener
import com.example.may1.smarthelmet.R
import com.example.may1.smarthelmet.registerlogin.LoginActivity
import kotlinx.android.synthetic.main.activity_connect_via_bluetooth.*
import android.widget.ListView as ListView1


class ConnectViaBluetoothActivity : AppCompatActivity() {

    // declare button for launching website and textview for connection status
    internal var tlbutton: Button? = null
    internal lateinit var textView1: TextView

    // Member fields
    private var mBtAdapter: BluetoothAdapter? = null

    // Set up on-click listener for the list (nicked this - unsure)
    private val mDeviceClickListener = OnItemClickListener { av, v, arg2, arg3 ->
        textView1.text = "Connecting..."
        // Get the device MAC address, which is the last 17 chars in the View
        val info = (v as TextView).text.toString()
        val address = info.substring(info.length - 17)

        // Make an intent to start next activity while taking an extra which is the MAC address.
        val i = Intent(this@ConnectViaBluetoothActivity, DashboardActivity::class.java)
        i.putExtra(EXTRA_DEVICE_ADDRESS, address)
        startActivity(i)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect_via_bluetooth)
        back_to_login_btn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onResume() {
        super.onResume()
        //***************
        checkBTState()

        textView1 = findViewById(R.id.connecting)
        textView1.textSize = 40f
        textView1.text = " "

        // Initialize array adapter for paired devices
        val mPairedDevicesArrayAdapter = ArrayAdapter<String>(this,
            R.layout.device_name
        )

        // Find and set up the ListView for paired devices
        val pairedListView = findViewById<ListView1>(R.id.paired_devices)
        pairedListView.adapter = mPairedDevicesArrayAdapter
        pairedListView.onItemClickListener = mDeviceClickListener

        // Get the local Bluetooth adapter
        mBtAdapter = BluetoothAdapter.getDefaultAdapter()

        // Get a set of currently paired devices and append to 'pairedDevices'
        val pairedDevices = mBtAdapter!!.bondedDevices

        // Add previosuly paired devices to the array
        if (pairedDevices.size > 0) {
           // findViewById<View>(R.id.title_paired_devices).visibility = View.VISIBLE//make title viewable
            for (device in pairedDevices) {
                mPairedDevicesArrayAdapter.add(device.name + "\n" + device.address)
            }
        } else {
            mPairedDevicesArrayAdapter.add("no devices paired")
        }
    }

    private fun checkBTState() {
        // Check device has Bluetooth and that it is turned on
        mBtAdapter = BluetoothAdapter.getDefaultAdapter() // CHECK THIS OUT THAT IT WORKS!!!
        if (mBtAdapter == null) {
            Toast.makeText(baseContext, "Device does not support Bluetooth", Toast.LENGTH_SHORT).show()
        } else {
            if (mBtAdapter!!.isEnabled) {
                Log.d(TAG, "...Bluetooth ON...")
            } else {
                //Prompt user to turn on Bluetooth
                val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
                startActivityForResult(enableBtIntent, 1)

            }
        }
    }

    companion object {
        // Debugging for LOGCAT
        private val TAG = "DeviceListActivity"
        private val D = true

        // EXTRA string to send on to mainactivity
        var EXTRA_DEVICE_ADDRESS = "device_address"
    }
}


