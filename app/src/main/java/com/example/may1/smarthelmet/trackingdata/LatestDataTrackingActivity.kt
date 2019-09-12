package com.example.may1.smarthelmet.trackingdata

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import com.example.may1.smarthelmet.Class.UpdateData
import com.example.may1.smarthelmet.Class.User
import com.example.may1.smarthelmet.MusicDashboardActivity
import com.example.may1.smarthelmet.R
import com.example.may1.smarthelmet.SettingsDashboardActivity
import com.example.may1.smarthelmet.connectbluetooth.DashboardActivity
import com.example.may1.smarthelmet.registerlogin.RegisterActivity
import com.example.may1.smarthelmet.viewstrackingdata.LatestDataRow
import com.google.android.gms.nearby.messages.internal.Update
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_latest_data_tracking.*
import kotlinx.android.synthetic.main.activity_tracking_activity.*
import kotlinx.android.synthetic.main.latest_message_row.view.*
import kotlinx.android.synthetic.main.new_user_tracking_activity.*

class LatestDataTrackingActivity : AppCompatActivity() {

    companion object {
        var currentUser: User? = null
        val TAG = "LatestMessages"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_latest_data_tracking)
//**********************************************navigate********************************************
        tracking_music_activity.setOnClickListener {
            val intent = Intent(this, MusicDashboardActivity::class.java)
            startActivity(intent)
        }

        tracking_home_activity.setOnClickListener {
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
        }

        tracking_settings_activity.setOnClickListener {
            val intent = Intent(this, SettingsDashboardActivity::class.java)
            startActivity(intent)
        }

        tracking_map_activity.setOnClickListener {
            val launchIntent = packageManager.getLaunchIntentForPackage("com.google.android.apps.maps")
            if (launchIntent != null) {
                startActivity(launchIntent)//null pointer check in case package name was not found
            }
            Log.d("AAA","running")
        }
        navigate_latest_music_btn.setOnClickListener {
            val intent = Intent(this,MusicDashboardActivity::class.java)
            startActivity(intent)
        }
        navigate_latest_newtracking_btn.setOnClickListener {
            val intent = Intent(this,NewUserTrackingActivity::class.java)
            startActivity(intent)
        }

//**************************************************************************************************


        recyclerview_latest_update.adapter = adapter
        recyclerview_latest_update.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        // set item click listener on your adapter
        adapter.setOnItemClickListener { item, view ->
            Log.d(TAG, "123")
            val intent = Intent(this, TrackingActivity::class.java)

            // we are missing the chat partner user

            val row = item as LatestDataRow
            intent.putExtra(NewUserTrackingActivity.USER_KEY, row.updatePartnerUser)
            //user_key.setText("fuck:"+ NewUserTrackingActivity.USER_KEY.toString())
            startActivity(intent)
        }

//    setupDummyRows()
        listenForLatestMessages()

        fetchCurrentUser()

        verifyUserIsLoggedIn()
}

    val latestMessagesMap = HashMap<String, UpdateData>()

    private fun refreshRecyclerViewMessages() {
        adapter.clear()
        latestMessagesMap.values.forEach {
            adapter.add(LatestDataRow(it))
        }
    }

    private fun listenForLatestMessages() {
        val fromId = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/latest-messages/$fromId")
        ref.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(p0: DataSnapshot, p1: String?) {
                val updateData = p0.getValue(UpdateData::class.java) ?: return
                latestMessagesMap[p0.key!!] = updateData
                refreshRecyclerViewMessages()
            }

            override fun onChildChanged(p0: DataSnapshot, p1: String?) {
                val updateData = p0.getValue(UpdateData::class.java) ?: return
                latestMessagesMap[p0.key!!] = updateData
                refreshRecyclerViewMessages()
            }

            override fun onChildMoved(p0: DataSnapshot, p1: String?) {

            }
            override fun onChildRemoved(p0: DataSnapshot) {

            }
            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    val adapter = GroupAdapter<ViewHolder>()

//  private fun setupDummyRows() {
//
//
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//    adapter.add(LatestMessageRow())
//  }

    private fun fetchCurrentUser() {
        val uid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                currentUser = p0.getValue(User::class.java)
               // Log.d("LatestMessages", "Current user ${currentUser?.profileImageUrl}")
            }

            override fun onCancelled(p0: DatabaseError) {

            }
        })
    }

    private fun verifyUserIsLoggedIn() {
        val uid = FirebaseAuth.getInstance().uid
        if (uid == null) {
            val intent = Intent(this, RegisterActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }
    }

//    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
//        when (item?.itemId) {
//            R.id.menu_new_message -> {
//                val intent = Intent(this, NewMessageActivity::class.java)
//                startActivity(intent)
//            }
//            R.id.menu_sign_out -> {
//                FirebaseAuth.getInstance().signOut()
//                val intent = Intent(this, RegisterActivity::class.java)
//                intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK.or(Intent.FLAG_ACTIVITY_NEW_TASK)
//                startActivity(intent)
//            }
//        }
//
//        return super.onOptionsItemSelected(item)
//    }

//    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
//        menuInflater.inflate(R.menu.nav_menu, menu)
//        return super.onCreateOptionsMenu(menu)
//    }

}

